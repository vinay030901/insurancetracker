package com.sundefined.insurancetracker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sundefined.insurancetracker.model.PolicyEvent;
import com.sundefined.insurancetracker.service.WebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
@Slf4j
public class WebhookController {

    private final WebhookService service;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${webhook.secret}")
    private String webhookSecret;

    @PostMapping("/event")
    public ResponseEntity<?> receiveEvent(
            @RequestHeader(value = "X-Request-Id", required = false) String reqId,
            @RequestHeader(value = "X-Signature", required = false) String signature,
            @RequestHeader(value = "X-Timestamp", required = false) String ts,
            @RequestBody String body) {

        log.info("[WebhookController] Incoming webhook reqId={} ts={} body={}", reqId, ts, body);

        if (reqId == null || reqId.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing X-Request-Id header"));
        }

        try {
            var result = service.verifyAndSave(reqId, signature, ts, body);

            if (result.isDuplicate()) {
                log.info("[WebhookController] Duplicate request detected reqId={}", reqId);
                return ResponseEntity.ok(Map.of("status", "duplicate", "reqId", reqId));
            } else {
                PolicyEvent saved = result.getSavedEvent();
                log.info("[WebhookController] Event processed successfully reqId={} policyId={} stage={} completed={}",
                        reqId, saved.getPolicyId(), saved.getStage(), saved.isCompleted());

                return ResponseEntity.status(HttpStatus.CREATED).body(saved);
            }

        } catch (WebhookService.InvalidSignatureException e) {
            log.error("[WebhookController] Invalid signature for reqId={}", reqId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "invalid_signature"));

        } catch (WebhookService.TimeStampException e) {
            log.error("[WebhookController] Invalid timestamp for reqId={}", reqId);
            return ResponseEntity.badRequest().body(Map.of("error", "invalid_timestamp"));

        } catch (Exception e) {
            log.error("[WebhookController] Internal error processing reqId={}", reqId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "internal_error", "message", e.getMessage()));
        }
    }
}
