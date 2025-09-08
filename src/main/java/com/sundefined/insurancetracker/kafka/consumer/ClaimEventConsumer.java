package com.sundefined.insurancetracker.kafka.consumer;

import com.sundefined.insurancetracker.model.PolicyEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ClaimEventConsumer {

    @KafkaListener(topics = "policy-claim-events", groupId = "insurance-claim-group")
    public void consume(PolicyEvent event) {
        System.out.println("Claim event received: " + event);
        // TODO: business logic for claims
    }
}
