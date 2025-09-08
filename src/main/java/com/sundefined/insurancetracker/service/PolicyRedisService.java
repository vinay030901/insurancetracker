package com.sundefined.insurancetracker.service;

import com.sundefined.insurancetracker.model.PolicyEvent;
import com.sundefined.insurancetracker.repository.PolicyEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${app.cache.ttl-hours:24}")
    private long ttlHours;

    private Duration getTtl() {
        return Duration.ofHours(ttlHours);
    }
    public void setPolicy(String policyId,PolicyEvent policyEvent){
        redisTemplate.opsForValue().set(policyId, policyEvent, getTtl());
    }
    public PolicyEvent getPolicy(String policyId){
        return (PolicyEvent) redisTemplate.opsForValue().get(policyId);
    }

    public void saveCurrentStage(String policyId, String stage) {
        String key = "policy:" + policyId + ":currentStage";
        redisTemplate.opsForValue().set(key, stage, getTtl());
        log.info("Saved current stage [{}] for policy [{}] with TTL {}h", stage, policyId, ttlHours);
    }

    public String getCurrentStage(String policyId) {
        String stage = (String) redisTemplate.opsForValue().get("policy:" + policyId + ":currentStage");
        log.info("Fetched current stage [{}] for policy [{}]", stage, policyId);
        return stage;
    }

    public void saveCompletedStatus(String policyId, boolean completed) {
        String key = "policy:" + policyId + ":completed";
        redisTemplate.opsForValue().set(key, completed, getTtl());
        log.info("Saved completed status [{}] for policy [{}] with TTL {}h", completed, policyId, ttlHours);
    }

    public Boolean isCompleted(String policyId) {
        Boolean completed = (Boolean) redisTemplate.opsForValue().get("policy:" + policyId + ":completed");
        log.info("Fetched completed status [{}] for policy [{}]", completed, policyId);
        return completed;
    }

    public void deletePolicyCache(String policyId) {
        redisTemplate.delete("policy:" + policyId + ":currentStage");
        redisTemplate.delete("policy:" + policyId + ":completed");
        log.info("Deleted Redis cache for policy [{}]", policyId);
    }
}
