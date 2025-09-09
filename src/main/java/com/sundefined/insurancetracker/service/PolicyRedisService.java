package com.sundefined.insurancetracker.service;

import com.sundefined.insurancetracker.model.Policy;
import com.sundefined.insurancetracker.model.PolicyEvent;
import com.sundefined.insurancetracker.model.User;
import com.sundefined.insurancetracker.model.UserRequestDto;
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

    public Policy getPolicy(String policyId){
        return (Policy) redisTemplate.opsForValue().get(policyId);
    }
    public void setPolicy(String policyId, Policy policy){
        redisTemplate.opsForValue().set(policyId, policy);
    }
    public void setPolicyEvent(String policyId,PolicyEvent policyEvent){
        redisTemplate.opsForValue().set(policyId, policyEvent);
    }
    public PolicyEvent getPolicyEvent(String policyId){
        return (PolicyEvent) redisTemplate.opsForValue().get(policyId);
    }
    public User getUser(UserRequestDto userRequestDto){
        return (User) redisTemplate.opsForValue().get(userRequestDto.getUserId());
    }
    public void setUser(User user){
        redisTemplate.opsForValue().set(user.getUserId(), user);
    }

    public void deletePolicyCache(String policyId) {
        redisTemplate.delete("policy:" + policyId + ":currentStage");
        redisTemplate.delete("policy:" + policyId + ":completed");
        log.info("Deleted Redis cache for policy [{}]", policyId);
    }
}
