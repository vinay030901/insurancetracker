package com.sundefined.insurancetracker.workflow;

import java.util.*;

public class StageRegistry {

    private static final Map<String, List<String>> stages = new HashMap<>();

    static {
        stages.put("health", List.of("application_submitted", "underwriting", "medical_checkup", "approval", "completed"));
        stages.put("car", List.of("application_submitted", "document_verification", "vehicle_survey", "approval", "completed"));
        stages.put("life", List.of("application_submitted", "background_check", "risk_assessment", "approval", "completed"));
    }

    public static Optional<String> nextStage(String insuranceType, String currentStage) {
        List<String> typeStages = stages.get(insuranceType.toLowerCase());
        if (typeStages == null) return Optional.empty();
        int idx = typeStages.indexOf(currentStage);
        if (idx == -1 || idx == typeStages.size() - 1) return Optional.empty(); // no next
        return Optional.of(typeStages.get(idx + 1));
    }

    public static boolean isValidStage(String insuranceType, String stage) {
        List<String> typeStages = stages.get(insuranceType.toLowerCase());
        return typeStages != null && typeStages.contains(stage);
    }

    public static List<String> getStages(String insuranceType) {
        return stages.getOrDefault(insuranceType.toLowerCase(), List.of());
    }
}
