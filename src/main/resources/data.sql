-- HEALTH insurance subscribers
INSERT INTO webhook_subscriber (insurance_type, stage, callback_url)
SELECT 'HEALTH', 'underwriting', 'http://localhost:8085/underwriting/callback'
WHERE NOT EXISTS (
    SELECT 1 FROM webhook_subscriber
    WHERE insurance_type='HEALTH' AND stage='underwriting'
);

INSERT INTO webhook_subscriber (insurance_type, stage, callback_url)
SELECT 'HEALTH', 'medical_checkup', 'http://localhost:8085/medical/callback'
WHERE NOT EXISTS (
    SELECT 1 FROM webhook_subscriber
    WHERE insurance_type='HEALTH' AND stage='medical_checkup'
);

INSERT INTO webhook_subscriber (insurance_type, stage, callback_url)
SELECT 'HEALTH', 'approval', 'http://localhost:8085/health-verification/callback'
WHERE NOT EXISTS (
    SELECT 1 FROM webhook_subscriber
    WHERE insurance_type='HEALTH' AND stage='approval'
);

-- CAR insurance subscribers
INSERT INTO webhook_subscriber (insurance_type, stage, callback_url)
SELECT 'CAR', 'document_verification', 'http://localhost:8086/doc-verifier/callback'
WHERE NOT EXISTS (
    SELECT 1 FROM webhook_subscriber
    WHERE insurance_type='CAR' AND stage='document_verification'
);

INSERT INTO webhook_subscriber (insurance_type, stage, callback_url)
SELECT 'CAR', 'vehicle_survey', 'http://localhost:8086/surveyor/callback'
WHERE NOT EXISTS (
    SELECT 1 FROM webhook_subscriber
    WHERE insurance_type='CAR' AND stage='vehicle_survey'
);

INSERT INTO webhook_subscriber (insurance_type, stage, callback_url)
SELECT 'CAR', 'approval', 'http://localhost:8086/approvals/callback'
WHERE NOT EXISTS (
    SELECT 1 FROM webhook_subscriber
    WHERE insurance_type='CAR' AND stage='approval'
);

-- LIFE insurance subscribers
INSERT INTO webhook_subscriber (insurance_type, stage, callback_url)
SELECT 'LIFE', 'background_check', 'http://localhost:8087/background/callback'
WHERE NOT EXISTS (
    SELECT 1 FROM webhook_subscriber
    WHERE insurance_type='LIFE' AND stage='background_check'
);

INSERT INTO webhook_subscriber (insurance_type, stage, callback_url)
SELECT 'LIFE', 'risk_assessment', 'http://localhost:8087/risk/callback'
WHERE NOT EXISTS (
    SELECT 1 FROM webhook_subscriber
    WHERE insurance_type='LIFE' AND stage='risk_assessment'
);

INSERT INTO webhook_subscriber (insurance_type, stage, callback_url)
SELECT 'LIFE', 'approval', 'http://localhost:8087/life-approvals/callback'
WHERE NOT EXISTS (
    SELECT 1 FROM webhook_subscriber
    WHERE insurance_type='LIFE' AND stage='approval'
);
