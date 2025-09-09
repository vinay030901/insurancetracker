package com.sundefined.insurancetracker.repository;

import com.sundefined.insurancetracker.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
    Optional<Policy> findByPolicyId(String policyId);
}
