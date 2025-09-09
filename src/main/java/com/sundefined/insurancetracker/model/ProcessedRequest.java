package com.sundefined.insurancetracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "processed_requests", indexes = {
        @Index(name = "idx_reqid", columnList = "reqId", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessedRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reqId;

    private String policyId; // optional link to saved event

    // private Instant processedAt;
}
