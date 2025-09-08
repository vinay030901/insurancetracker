package com.sundefined.insurancetracker.kafka.dispatcher;

import com.sundefined.insurancetracker.model.PolicyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventDispatcher {

    private final KafkaTemplate<String, PolicyEvent> kafkaTemplate;

    public void dispatch(PolicyEvent event) {
        String topic = resolveTopic(event.getInsuranceType());
        log.info("[Kafka][Dispatcher] Sending current stage '{}' for policyId={} to topic={}",
                event.getStage(), event.getPolicyId(), topic);
        kafkaTemplate.send(topic, event.getPolicyId(), event);
    }

    private String resolveTopic(String insuranceType) {
        // One topic per insurance type for simplicity
        return "policy-" + insuranceType.toLowerCase() + "-events";
    }
}
