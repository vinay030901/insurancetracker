package com.sundefined.insurancetracker.repository;

import com.sundefined.insurancetracker.model.PolicyEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyEventRepository extends JpaRepository<PolicyEvent, Long> {
    List<PolicyEvent> findByPolicyId(String policyId);
}
