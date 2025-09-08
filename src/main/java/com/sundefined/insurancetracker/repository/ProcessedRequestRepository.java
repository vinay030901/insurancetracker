package com.sundefined.insurancetracker.repository;

import com.sundefined.insurancetracker.model.ProcessedRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcessedRequestRepository extends JpaRepository<ProcessedRequest, Long> {
    Optional<ProcessedRequest> findByReqId(String reqId);
}
