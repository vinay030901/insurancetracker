package com.sundefined.insurancetracker.service;

import com.sundefined.insurancetracker.model.PolicyEvent;
import com.sundefined.insurancetracker.model.TeamSubscription;
import com.sundefined.insurancetracker.repository.TeamSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final TeamSubscriptionRepository subscriptionRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public void notifyTeamForStage(PolicyEvent event, String nextStage) {
        List<TeamSubscription> subs = subscriptionRepository.findByInsuranceTypeAndStage(
                event.getInsuranceType().toUpperCase(),
                nextStage.toLowerCase()
        );

        if (subs.isEmpty()) {
            log.warn("[Notification] No team subscriptions found for insuranceType={} nextStage={}",
                    event.getInsuranceType(), nextStage);
            return;
        }

        // Clone the event so that outgoing webhook has nextStage instead of current stage
        PolicyEvent outboundEvent = PolicyEvent.builder()
                .id(event.getId())
                .policyId(event.getPolicyId())
                .insuranceType(event.getInsuranceType())
                .stage(nextStage)  // important: send nextStage in outbound payload
                .description(event.getDescription())
//                .createdAt(event.getCreatedAt())
                .sourceSystemId(event.getSourceSystemId())
                .externalEvent(event.getExternalEvent())
                .completed(event.isCompleted())
                .build();

        for (TeamSubscription sub : subs) {
            try {
                log.info("[Notification] → Calling team={} at {} with policyId={} for nextStage={}",
                        sub.getTeamName(), sub.getCallbackUrl(), event.getPolicyId(), nextStage);

//                restTemplate.postForEntity(sub.getCallbackUrl(), outboundEvent, Void.class);

                log.info("[Notification] ✔ Successfully called team={} for policyId={} at nextStage={}",
                        sub.getTeamName(), event.getPolicyId(), nextStage);
            } catch (Exception e) {
                log.error("[Notification] ✖ Failed to notify team={} at {} for policyId={}",
                        sub.getTeamName(), sub.getCallbackUrl(), event.getPolicyId(), e);
            }
        }
    }
}



// Keeping old method in case we want bulk notifications
//    public void notifyTeams(PolicyEvent event) {
//        List<TeamSubscription> subs =
//                subscriptionRepository.findByInsuranceTypeAndStage(
//                        event.getInsuranceType(),
//                        event.getStage()
//                );
//
//        for (TeamSubscription sub : subs) {
//            try {
//                log.info("Notifying {} at {}", sub.getTeamName(), sub.getCallbackUrl());
//                restTemplate.postForEntity(sub.getCallbackUrl(), event, Void.class);
//            } catch (Exception e) {
//                log.error("Failed to notify {} at {}", sub.getTeamName(), sub.getCallbackUrl(), e);
//            }
//        }
//    }

