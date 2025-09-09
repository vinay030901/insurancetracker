package com.sundefined.insurancetracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "policies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String policyId;

    private String insuranceType; // HEALTH, CAR, LIFE

    private String currentStage; // application_submitted, background_check, etc.

    // @Builder.Default
    // private Instant createdAt = Instant.now();
    // @Builder.Default
    // private Instant updatedAt = Instant.now();

    @Builder.Default
    private boolean completed = false; // whether this policy has reached "completed" stage

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
