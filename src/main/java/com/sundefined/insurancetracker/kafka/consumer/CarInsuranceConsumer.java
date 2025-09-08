package com.sundefined.insurancetracker.kafka.consumer;

import com.sundefined.insurancetracker.model.PolicyEvent;
import com.sundefined.insurancetracker.repository.PolicyEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CarInsuranceConsumer {

    private final PolicyEventRepository repository;

    @KafkaListener(topics = "policy-car-events", groupId = "car-consumer-group")
    public void consume(PolicyEvent event) {
        log.info("[Kafka][CarConsumer] Received event for policyId={} at stage={}",
                event.getPolicyId(), event.getStage());
        repository.save(event);
        log.info("[Kafka][CarConsumer] Saved event for policyId={} at stage={}",
                event.getPolicyId(), event.getStage());
    }
}