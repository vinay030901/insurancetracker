package com.sundefined.insurancetracker.config;

import com.sundefined.insurancetracker.model.TeamSubscription;
import com.sundefined.insurancetracker.repository.TeamSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebhookSubscriberInitializer implements CommandLineRunner {

    private final TeamSubscriptionRepository repository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing webhook subscribers...");

        // HEALTH insurance subscribers
        saveIfNotExists("HEALTH", "medical_checkup", "http://localhost:8085/medical/callback");
        saveIfNotExists("HEALTH", "approval", "http://localhost:8085/health-verification/callback");

        // CAR insurance subscribers
        saveIfNotExists("CAR", "document_verification", "http://localhost:8086/doc-verifier/callback");
        saveIfNotExists("CAR", "vehicle_survey", "http://localhost:8086/surveyor/callback");
        saveIfNotExists("CAR", "approval", "http://localhost:8086/approvals/callback");

        // LIFE insurance subscribers
        saveIfNotExists("LIFE", "background_check", "http://localhost:8087/background/callback");
        saveIfNotExists("LIFE", "risk_assessment", "http://localhost:8087/risk/callback");
        saveIfNotExists("LIFE", "approval", "http://localhost:8087/life-approvals/callback");

        log.info("Webhook subscribers initialization completed.");
    }

    private void saveIfNotExists(String insuranceType, String stage, String callbackUrl) {
        List<TeamSubscription> existing = repository.findByInsuranceTypeAndStage(insuranceType, stage);
        if (existing.isEmpty()) {
            TeamSubscription subscription = TeamSubscription.builder()
                    .insuranceType(insuranceType)
                    .stage(stage)
                    .callbackUrl(callbackUrl)
                    .build();
            repository.save(subscription);
            log.info("Inserted webhook subscriber: {} - {} -> {}", insuranceType, stage, callbackUrl);
        } else {
            log.info("Webhook subscriber already exists: {} - {}", insuranceType, stage);
        }
    }
}
