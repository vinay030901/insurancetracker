package com.sundefined.insurancetracker.controllers;

import com.sundefined.insurancetracker.service.PolicyRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyRedisService policyRedisService;

    @GetMapping("/{policyId}/stage")
    public ResponseEntity<String> getCurrentStage(@PathVariable String policyId) {
        String stage = policyRedisService.getCurrentStage(policyId);
        return stage != null
                ? ResponseEntity.ok(stage)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/{policyId}/completed")
    public ResponseEntity<Boolean> isCompleted(@PathVariable String policyId) {
        Boolean completed = policyRedisService.isCompleted(policyId);
        return completed != null
                ? ResponseEntity.ok(completed)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{policyId}/cache")
    public ResponseEntity<Void> clearPolicyCache(@PathVariable String policyId) {
        policyRedisService.deletePolicyCache(policyId);
        return ResponseEntity.noContent().build();
    }
}

