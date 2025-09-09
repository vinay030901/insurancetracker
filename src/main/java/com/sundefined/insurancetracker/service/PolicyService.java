package com.sundefined.insurancetracker.service;

import com.sundefined.insurancetracker.model.Policy;
import com.sundefined.insurancetracker.model.PolicyEvent;
import com.sundefined.insurancetracker.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository repository;
    private final UserService userService;
    private final PolicyRedisService policyRedisService;

    public Policy getPolicy(String policyId) {
        Optional<Policy> policy = repository.findByPolicyId(policyId);
        if(policy.isPresent())
            return policy.get();
        throw new RuntimeException("Policy not found");
    }
    public void savePolicy(PolicyEvent policyEvent){
        Optional<Policy> policy = repository.findByPolicyId(policyEvent.getPolicyId());
        Policy newPolicy = policy.orElseGet(() -> Policy.builder()
                .policyId(policyEvent.getPolicyId())
                .insuranceType(policyEvent.getInsuranceType())
                .currentStage(policyEvent.getStage())
                .build());
        repository.save(newPolicy);

        policyRedisService.setPolicyEvent(policyEvent.getPolicyId(), policyEvent);
        policyRedisService.setPolicy(policyEvent.getPolicyId(), newPolicy);
        //  Attach user
        userService.attachUserToPolicy(newPolicy,policyEvent.getSourceSystemId());
    }
}
