1. Business/Technical Context
Insurance processes often involve multiple steps like submitting applications, document verification, medical checks, and approvals. These steps are usually managed manually or across scattered systems, which makes it hard for users to track progress and for teams to collaborate efficiently.

2. Problem Statement
Customers do not have an easy way to know the current status of their insurance policy. At the same time, internal teams struggle to record and track updates in a structured manner. This causes delays, confusion, and poor customer experience.


We built a real-time policy tracking system.

Customers can log in and see live progress of their insurance journey.

Internal teams can submit updates instantly through secure APIs.

The system is scalable and can handle thousands of policies in parallel.

Webhooks

Act like a doorbell notification from different insurance teams.

The moment a team finishes a step (medical test, verification, etc.), the system gets notified instantly.

Removes waiting time and manual reporting.

Kafka

Handles heavy traffic smoothly.

Makes sure updates are processed in correct order.

Prevents system crashes when thousands of updates come in together.

Redis

Works as a fast memory cache.

Frequently accessed policy data is served in milliseconds instead of hitting the database every time.

Improves app speed drastically.

Security (Signature + Timestamp)

Every incoming event carries a digital signature and timestamp.

Prevents fake updates and replay attacks.

Ensures that only trusted teams can push events.

4. Customer Benefit

Transparency: Customers always know the exact stage of their policy.

Speed: Updates appear instantly after a team finishes its task.

Trust: Customers are confident the process is secure and tamper-proof.

Scalability: System can grow with the business without slowing down.

Policy Tracking: Before vs After Our Solution
Aspect	Before (Traditional Way)	After (Our Solution) 🚀
Update Flow	Manual updates via calls/emails	Automatic updates via Webhooks
Customer View	Customer has to call support to ask status	Customer logs in → sees live progress bar
Internal Teams	Teams send reports manually, often delayed	Teams push updates instantly via secure API
Data Speed	Database hit every time → slow responses	Redis caching → milliseconds response
Scalability	Breaks under high load (thousands of policies)	Kafka ensures smooth high-volume handling
Security	Vulnerable to fake or tampered updates	Signature + Timestamp → verified events
Transparency	Customers in the dark, no clear visibility	Real-time, stage-by-stage transparency

Non-Quantifiable Benefits of the Proposed Solution

Tentative Efforts Saved

Drastic reduction in back-and-forth emails/calls between customer and support.

Teams no longer need to manually push updates — saving hours of coordination.

Quality Improvement

Status updates are now consistent and error-free (no human mistakes in entering stages).

Real-time updates improve trust and customer satisfaction.

Manual Touchpoints Removed

Support team doesn’t need to act as middlemen → direct visibility for customers.

Internal handovers happen automatically (next stage team notified instantly).

Transparency & Accountability

Every step is timestamped and traceable, improving auditability.

Customers get clear stage-wise progress, reducing disputes.

Scalability of Operations

The system can handle thousands of policies in parallel without adding manpower.

Employee Productivity

Teams focus on actual decision-making (like risk assessment, approvals) instead of clerical updates.


Innovative / Out-of-the-Box Ideas

Webhooks – Real-time updates, no manual follow-ups.

Kafka – Scalable event distribution to multiple systems.

Redis – Instant tracking, faster than DB queries.

Security – HMAC signatures + timestamp checks.

Canonical Stage Registry – Standardized stages per insurance type.

eKart-style Tracking – Simple, transparent customer experience.



🛡 Insurance Tracker – Workflow Automation with Kafka, Redis & Postgres

> Hackathon Project – Automating insurance claim workflows with real-time event tracking, team notifications, and resilient processing pipeline.




---

🚀 Problem Statement

Insurance claims involve multiple stages (like document submission, verification, approval, payment) and different teams (health, car, etc.).
Currently, workflows are:

Manual → prone to delays & errors

Opaque → customers and managers don’t know claim status in real time

Non-resilient → duplicate requests, missed events, and poor monitoring



---

💡 Our Solution

We built a real-time workflow orchestration system using Spring Boot, Kafka, Redis, and Postgres.

✅ Ingest webhooks from external systems (claim requests, updates)
✅ Ensure idempotency & authenticity (HMAC + timestamp checks)
✅ Store events in Postgres (audit trail + persistence)
✅ Use Kafka to asynchronously dispatch events to the right teams (health, car, etc.)
✅ Use Redis caching for quick lookups of current claim stage and completion status
✅ Provide REST APIs to query claim progress instantly
✅ Send team notifications for next actions
✅ Support colored logs for easy debugging during hackathon demos


---

⚙ Architecture

┌──────────────┐
   External ---> │  Webhook API │ ---+
    Systems      └──────────────┘    |
                                      v
                              ┌──────────────┐
                              │ Verification │  (HMAC, Timestamp, Idempotency)
                              └──────────────┘
                                      |
                                      v
                        ┌─────────────────────────┐
                        │ Postgres (PolicyEvents) │
                        └─────────────────────────┘
                                      |
                        ┌─────────────────────────┐
                        │ Redis Cache (fast read) │
                        └─────────────────────────┘
                                      |
                                      v
                        ┌─────────────────────────┐
                        │ Kafka Event Dispatcher  │
                        └─────────────────────────┘
                        /           |            \
                       v            v             v
                [Health Consumer] [Car Consumer] [Notifications]


---

🛠 Tech Stack

Backend: Spring Boot 3 (Java 17)

Database: PostgreSQL (event persistence)

Cache: Redis (fast lookup, TTL-based cache)

Messaging: Apache Kafka (internal async communication)

Security: HMAC validation + duplicate request handling

Logging: Colored console logs for observability

Tools: Docker Compose (Postgres, Redis, Kafka setup)



---

📌 Features

🔒 Secure Webhook Processing – HMAC + timestamp verification

♻ Idempotency – Duplicate webhooks ignored

📊 Persistent Audit Trail – Every claim event stored in Postgres

⚡ Low-Latency APIs – Claim status served from Redis

📨 Kafka Consumers – Separate channels for health & car claims

🔔 Notification Service – Notifies next responsible team

🖥 Hackathon-Friendly Logs – Colored, human-readable



---

🔧 Setup & Run

1. Clone Repository

git clone https://github.com/your-repo/insurancetracker.git
cd insurancetracker

2. Run Infra (Postgres, Redis, Kafka)

docker-compose up -d

3. Configure Application

Update .env or export variables:

export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/tracker
export SPRING_DATASOURCE_USERNAME=tracker
export SPRING_DATASOURCE_PASSWORD=trackerpass
export SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:29092
export SPRING_REDIS_HOST=localhost
export WEBHOOK_SECRET=supersecret123

4. Run Application

./mvnw spring-boot:run


---

📡 API Endpoints

Webhook Receiver

POST /webhook
Headers:
  X-Request-Id: <unique-id>
  X-Signature: sha256=<hmac>
  X-Timestamp: 2025-09-08T12:00:00Z
Body:
  { "policyId": "P-123", "insuranceType": "CAR", "stage": "INITIATED" }

Query Claim Status

GET /policy/{policyId}/stage
GET /policy/{policyId}/completed


---

📈 Demo Flow

1. External system sends claim webhook (INITIATED).


2. System validates request → stores in DB → caches in Redis.


3. Kafka dispatches event → right consumer picks it.


4. Notification sent to next team (VERIFICATION).


5. Hackathon judges query API → see real-time status updates.




---

🏆 Why This Project?

Solves a real-world industry problem (insurance claim delays)

Uses modern event-driven architecture

Demonstrates full-stack backend engineering (DB, cache, messaging, APIs, security)

Scalable, fault-tolerant, resilient system

Hackathon-ready demo with colored logs and instant API responses



---

👥 Team

Vinay, Arun, Harshita, Kashvi



---

🚀 Future Enhancements

Add dashboard UI to visualize claim progress

Integrate SMS/Email notifications for customers

Deploy on Kubernetes + Cloud (AWS/GCP)

Implement Retry + Dead Letter Queue (DLQ) in Kafka

Advanced monitoring with Prometheus & Grafana
