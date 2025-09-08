package com.sundefined.insurancetracker.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teamName;       // e.g. "Medical", "CarInspection"
    private String insuranceType;  // HEALTH, CAR, LIFE
    private String stage;          // Which stage they want notification for
    private String callbackUrl;    // Webhook endpoint of the team
}
