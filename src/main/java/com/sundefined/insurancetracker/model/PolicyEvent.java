package com.sundefined.insurancetracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity(name = "policy_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String policyId;

    private String insuranceType;   // HEALTH, CAR, LIFE

    private String stage;           // application_submitted, background_check, etc.

    private String description;

    @Builder.Default
    private Instant createdAt = Instant.now();

    // NEW FIELDS
    private String sourceSystem;    // e.g. "background-check-team", "risk-assessment-team"
    private String externalEvent;   // raw webhook event like "background_check_submitted"

    @Builder.Default
    private boolean completed = false; // whether this policy has reached "completed" stage
}

