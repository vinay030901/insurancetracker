package com.sundefined.insurancetracker.model;

import java.util.HashMap;
import java.util.Map;

public class StageEventMapper {

    private static final Map<String, String> externalToStage = new HashMap<>();

    static {
        // life insurance external event → next stage
        externalToStage.put("application_submitted", "background_check");
        externalToStage.put("background_check_submitted", "risk_assessment");
        externalToStage.put("risk_assessment_submitted", "approval");
        externalToStage.put("approval_submitted", "completed");

        // health insurance (example, can extend)
        externalToStage.put("underwriting_submitted", "medical_checkup");
        externalToStage.put("medical_checkup_submitted", "approval");
        externalToStage.put("approval_submitted_health", "completed");

        // car insurance (example)
        externalToStage.put("document_verification_submitted", "vehicle_survey");
        externalToStage.put("vehicle_survey_submitted", "approval");
        externalToStage.put("approval_submitted_car", "completed");
    }

    public static String getNextStage(String externalEvent) {
        return externalToStage.get(externalEvent.toLowerCase());
    }
}
