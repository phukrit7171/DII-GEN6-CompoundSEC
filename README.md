# DII-GEN6-CompoundSEC
This is project-based learning for CAMT-DII gen6 students

We will incrementally utilze OOP concept to develop the security system for a compound (resident buildings).
The importances of the project is allow student to do
1. Self-learning: read the related course materials in advance of lecture and learn to associate concept of lecture to the actual code using issue on Github
2. Backlog refinement: with question from self-learning, student must draft the issue on the backlog of current iteration. This draft of backlog will be discussed in class.
3. Dev-ready card: at the end of lecture session, student should clear all issues with lecturer and TA to fully understand the cost and process to move the card to ready tag.
4. In progress and In review: are tags that allow lecturer and TA to consult and review with student on the lab session.
5. Done: after reviewed, student can move the issues to the done tag to conclude the finished task on the current iteration.

**Backlog refinement** is the important step for student. The backlog can only create by student who did the self-study and develop the project to some extend.

# Design Brief
1. Access control system for multi-floor system (low floor, medium floor, high floor)
- Access cards with multi-facades id and time-based encryption
- Floor level access control and room level access control
2. Audit trial for card access
- Each attempts will be logged with necessary info
- Card generation or modification will be logged with necessary info
3. Card management 
- Add, modify, revoke permission of each card


# System Design
```
Compound Security System (Java Project Structure)
├── com (root package)
│   └── securitysystem (main package)
│       ├── core (Subsystem: Core Entities and Logic)
│       │   ├── Card.java (Class - Entity)
│       │   ├── Floor.java (Class - Entity)
│       │   ├── Room.java (Class - Entity)
│       │   ├── Location.java (Interface - Contract for Floor & Room)
│       │   ├── AccessControlSystem.java (Class - Central Controller)
│       │   ├── AccessDecision.java (Enum - Represent Access Decision)
│       │   ├── validators (Package - Card Validation Logic)
│       │   │   ├── CardValidator.java (Interface - Contract for Card Validation)
│       │   │   ├── BasicCardValidator.java (Class - Concrete Card Validator)
│       │   │   ├── TimeBasedCardValidator.java (Class - Concrete Time-Based Validator - if applicable)
│       │   ├── permissions (Package - Permission Checking Logic)
│       │   │   ├── PermissionChecker.java (Interface - Contract for Permission Checks)
│       │   │   ├── BasicPermissionChecker.java (Class - Concrete Permission Checker)
│       │   ├── events (Package - Events within Core System)
│       │   │   ├── AccessRequestEvent.java (Class - Represents an access request event)
│       │   │   ├── AccessGrantedEvent.java (Class)
│       │   │   ├── AccessDeniedEvent.java (Class)
│       │   │   ├── CardEvent.java (Abstract Class - Base for Card related events)
│       │   │   ├── CardGeneratedEvent.java (Class)
│       │   │   ├── CardModifiedEvent.java (Class)
│       │   │   ├── CardRevokedEvent.java (Class)
│       ├── management (Subsystem: Card & Location Management)
│       │   ├── card (Package - Card Management Specific)
│       │   │   ├── CardManager.java (Interface - Contract for Card Management)
│       │   │   ├── DefaultCardManager.java (Class - Concrete Card Manager)
│       │   │   ├── CardStorage.java (Interface - Contract for Card Persistence)
│       │   │   ├── InMemoryCardStorage.java (Class - In-Memory Storage - for simple cases)
│       │   │   ├── FileCardStorage.java (Class - File-based Storage - example)
│       │   │   ├── DatabaseCardStorage.java (Class - Database Storage - example)
│       │   ├── location (Package - Location Management Specific)
│       │   │   ├── LocationManager.java (Interface - Contract for Location Management)
│       │   │   ├── DefaultLocationManager.java (Class - Concrete Location Manager)
│       │   │   ├── LocationStorage.java (Interface - Contract for Location Persistence)
│       │   │   ├── InMemoryLocationStorage.java (Class - In-Memory Location Storage)
│       │   │   ├── FileLocationStorage.java (Class - File-based Location Storage)
│       │   │   ├── DatabaseLocationStorage.java (Class - Database Location Storage)
│       ├── logging (Subsystem: Audit Logging)
│       │   ├── AuditLogger.java (Interface - Contract for Audit Logging)
│       │   ├── DefaultAuditLogger.java (Class - Concrete Audit Logger)
│       │   ├── LogEntry.java (Class - Data class for log entries)
│       │   ├── LogStorage.java (Interface - Contract for Log Persistence)
│       │   ├── FileLogStorage.java (Class - File-based Log Storage)
│       │   ├── DatabaseLogStorage.java (Class - Database Log Storage)
│       ├── admin (Subsystem: Administration Interface - Conceptual)
│       │   ├── UserInterface.java (Interface - Abstract UI definition - if UI is part of Java code)
│       │   ├── CommandLineUI.java (Class - Example CLI implementation)
│       │   ├── SystemConfiguration.java (Class - Manages system settings)
│       │   ├── ReportingService.java (Interface - For reporting functionalities - optional)
│       │   ├── DefaultReportingService.java (Class - Concrete Reporting Service - optional)
│       ├── exceptions (Package - Custom Exceptions)
│       │   ├── AccessDeniedException.java (Class - Custom Exception)
│       │   ├── CardNotFoundException.java (Class - Custom Exception)
│       │   ├── LocationNotFoundException.java (Class - Custom Exception)
│       ├── main (Package - Entry Point)
│       │   ├── Main.java (Class - Contains the `main` method to start the application)
```