package com.sundefined.insurancetracker.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolicyStages {
    private static final Map<String, List<String>> stages = new HashMap<>();

    static {
        stages.put("health", List.of("application_submitted", "underwriting", "medical_checkup", "approval", "completed"));
        stages.put("car", List.of("application_submitted", "document_verification", "vehicle_survey", "approval", "completed"));
        stages.put("life", List.of("application_submitted", "background_check", "risk_assessment", "approval", "completed"));
    }

    public static List<String> getStages(String insuranceType) {
        return stages.getOrDefault(insuranceType.toLowerCase(), List.of());
    }
}
