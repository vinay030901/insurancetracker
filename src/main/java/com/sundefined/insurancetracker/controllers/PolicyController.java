package com.sundefined.insurancetracker.controllers;

import com.sundefined.insurancetracker.model.Policy;
import com.sundefined.insurancetracker.model.PolicyEvent;
import com.sundefined.insurancetracker.model.User;
import com.sundefined.insurancetracker.model.UserRequestDto;
import com.sundefined.insurancetracker.repository.PolicyEventRepository;
import com.sundefined.insurancetracker.repository.PolicyRepository;
import com.sundefined.insurancetracker.repository.UserRepository;
import com.sundefined.insurancetracker.service.PolicyRedisService;
import com.sundefined.insurancetracker.service.PolicyService;
import com.sundefined.insurancetracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PolicyController {

    private final PolicyRedisService policyRedisService;
    private final PolicyRepository policyRepository;
    private final UserService userService;
    private final PolicyService policyService;

    @PostMapping("/login")
    public ResponseEntity<?> getUser(@RequestBody UserRequestDto userRequest) {
        try{
            User user= userService.getUser(userRequest);
            return ResponseEntity.ok(user);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/userPolicies")
    public ResponseEntity<List<Policy>> getUserPolicies(@RequestBody UserRequestDto userRequest) {
        User user=userService.getUser(userRequest);
        List<Policy> policies=new ArrayList<>();
        for(String policyId:user.getPolicyIds()){
            policies.add(policyService.getPolicy(policyId));
        }
        return ResponseEntity.ok(policies);
    }
    @GetMapping("/{policyId}")
    public ResponseEntity<?> getPolicy(@PathVariable String policyId) {
        try{
            Policy policy;
            policy=policyRedisService.getPolicy(policyId);
            if(policy==null)
                policy=policyRepository.findByPolicyId(policyId).orElse(null);
            return ResponseEntity.ok(policy);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/{policyId}/cache")
    public ResponseEntity<Void> clearPolicyCache(@PathVariable String policyId) {
        policyRedisService.deletePolicyCache(policyId);
        return ResponseEntity.noContent().build();
    }
}

