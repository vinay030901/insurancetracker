package com.sundefined.insurancetracker.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic healthTopic() {
        return TopicBuilder.name("policy-health-events").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic carTopic() {
        return TopicBuilder.name("policy-car-events").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic lifeTopic() {
        return TopicBuilder.name("policy-life-events").partitions(3).replicas(1).build();
    }
}
