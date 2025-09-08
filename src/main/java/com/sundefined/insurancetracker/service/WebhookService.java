package com.sundefined.insurancetracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sundefined.insurancetracker.kafka.dispatcher.EventDispatcher;
import com.sundefined.insurancetracker.model.PolicyEvent;
import com.sundefined.insurancetracker.model.ProcessedRequest;
import com.sundefined.insurancetracker.repository.PolicyEventRepository;
import com.sundefined.insurancetracker.repository.ProcessedRequestRepository;
import com.sundefined.insurancetracker.workflow.StageRegistry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookService {

    private final PolicyEventRepository repository;
    private final ProcessedRequestRepository processedRequestRepository;
    private final PolicyRedisService policyRedisService;
    private final EventDispatcher dispatcher;
    private final NotificationService notificationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${webhook.secret}")
    private String secret;

    private static final Duration ALLOWED_SKEW = Duration.ofMinutes(5);

    public static class InvalidSignatureException extends RuntimeException {}
    public static class TimeStampException extends RuntimeException {}

    @Transactional
    public VerifyResult verifyAndSave(String reqId, String signatureHeader, String timestampHeader, String body) {
        // 1️⃣ Idempotency check
        Optional<ProcessedRequest> processed = processedRequestRepository.findByReqId(reqId);
        if (processed.isPresent()) {
            log.info("[WebhookService] Duplicate webhook request reqId={}", reqId);
            return VerifyResult.duplicate();
        }

        // 2️⃣ Timestamp check
        if (timestampHeader != null && !timestampHeader.isBlank()) {
            try {
                Instant t = Instant.parse(timestampHeader);
                Duration diff = Duration.between(t, Instant.now()).abs();
                if (diff.compareTo(ALLOWED_SKEW) > 0) {
                    log.warn("[WebhookService] Timestamp skew exceeded for reqId={}, skew={} minutes",
                            reqId, diff.toMinutes());
                    throw new TimeStampException();
                }
            } catch (TimeStampException e) {
                throw e;
            } catch (Exception e) {
                log.error("[WebhookService] Failed to parse timestamp header={} reqId={}", timestampHeader, reqId);
                throw new TimeStampException();
            }
        }

        try {
            // 3️⃣ Parse event
            PolicyEvent event = objectMapper.readValue(body, PolicyEvent.class);

            if (event.getPolicyId() == null || event.getPolicyId().isBlank()) {
                event.setPolicyId("P-" + UUID.randomUUID());
            }

            log.info("[WebhookService] Processing event reqId={} policyId={} stage={} externalEvent={} sourceSystem={}",
                    reqId, event.getPolicyId(), event.getStage(), event.getExternalEvent(), event.getSourceSystem());

            // 4️⃣ Determine next stage
            Optional<String> nextStage = StageRegistry.nextStage(
                    event.getInsuranceType(),
                    event.getStage()
            );

            // 5️⃣ Mark as completed if no next stage
            event.setCompleted(nextStage.isEmpty());

            // 6️⃣ Save event
            PolicyEvent saved = repository.save(event);
            log.info("[WebhookService] Saved event id={} policyId={} stage={} completed={}",
                    saved.getId(), saved.getPolicyId(), saved.getStage(), saved.isCompleted());

            // 7️⃣ Notify next stage team if exists
            nextStage.ifPresent(stage -> {
                log.info("[WebhookService] Next stage resolved={} for policyId={}", stage, saved.getPolicyId());
                notificationService.notifyTeamForStage(saved, stage);
            });

            // 8️⃣ Dispatch internally (Kafka/logging/etc.)
            dispatcher.dispatch(saved);

            // adding to redis
            policyRedisService.saveCurrentStage(saved.getPolicyId(), event.getStage());
            policyRedisService.saveCompletedStatus(saved.getPolicyId(), event.isCompleted());

            // 9️⃣ Record processed request
            ProcessedRequest pr = ProcessedRequest.builder()
                    .reqId(reqId)
                    .policyId(event.getPolicyId())
                    .processedAt(Instant.now())
                    .build();
            processedRequestRepository.save(pr);

            return VerifyResult.success(saved);

        } catch (InvalidSignatureException | TimeStampException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("[WebhookService] Exception while processing reqId={}", reqId, ex);
            throw new RuntimeException(ex);
        }
    }

    // Optional HMAC verification
    public boolean verifyHmac(String body, String signatureHeader) {
        if (signatureHeader == null || signatureHeader.isBlank()) return false;
        String sigHex = signatureHeader.startsWith("sha256=") ? signatureHeader.substring(7) : signatureHeader;
        byte[] expected;

        try {
            expected = hexStringToByteArray(sigHex);
        } catch (Exception e) {
            return false;
        }

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] computed = mac.doFinal(body.getBytes(StandardCharsets.UTF_8));
            return MessageDigest.isEqual(computed, expected);
        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        if (len % 2 != 0) throw new IllegalArgumentException("Invalid hex string");
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    @Getter
    public static final class VerifyResult {
        private final boolean duplicate;
        private final PolicyEvent savedEvent;

        private VerifyResult(boolean duplicate, PolicyEvent savedEvent) {
            this.duplicate = duplicate;
            this.savedEvent = savedEvent;
        }

        public static VerifyResult duplicate() {
            return new VerifyResult(true, null);
        }

        public static VerifyResult success(PolicyEvent savedEvent) {
            return new VerifyResult(false, savedEvent);
        }
    }
}
