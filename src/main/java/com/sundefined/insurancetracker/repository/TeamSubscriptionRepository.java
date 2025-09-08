package com.sundefined.insurancetracker.repository;

import com.sundefined.insurancetracker.model.TeamSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamSubscriptionRepository extends JpaRepository<TeamSubscription, Long> {
    List<TeamSubscription> findByInsuranceTypeAndStage(String insuranceType, String stage);
}
