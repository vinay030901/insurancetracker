package com.sundefined.insurancetracker.kafka.producer;

import com.sundefined.insurancetracker.kafka.KafkaTopics;
import com.sundefined.insurancetracker.model.PolicyEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PolicyEventProducer {

    private final KafkaTemplate<String, PolicyEvent> kafkaTemplate;

    public void sendClaimEvent(PolicyEvent event) {
        kafkaTemplate.send(KafkaTopics.CLAIM_EVENTS, event.getPolicyId(), event);
    }

    public void sendBuyEvent(PolicyEvent event) {
        kafkaTemplate.send(KafkaTopics.BUY_EVENTS, event.getPolicyId(), event);
    }
}
