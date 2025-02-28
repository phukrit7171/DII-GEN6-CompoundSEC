package com.camt.dii.secure.audit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Singleton implementation of the AuditLogger interface.
 * <p>
 * This class provides a centralized system for logging and retrieving audit information
 * throughout the access control system. It ensures a single, consistent audit trail
 * by implementing the Singleton design pattern, guaranteeing that all components
 * of the system log to the same audit repository.
 * </p>
 * 
 * <h3>Singleton Pattern Implementation</h3>
 * <p>
 * The Singleton pattern is implemented with the following components:
 * <ol>
 *   <li><b>Private static instance variable</b> - Ensures only one instance exists across the JVM</li>
 *   <li><b>Private constructor</b> - Prevents external instantiation of additional instances</li>
 *   <li><b>Public static access method</b> - Provides a global access point to the single instance</li>
 *   <li><b>Thread-safe initialization</b> - Uses synchronized method to handle concurrent access</li>
 * </ol>
 * </p>
 * 
 * <h3>Audit Trail Persistence</h3>
 * <p>
 * Audit records are:
 * <ul>
 *   <li>Stored in memory for fast retrieval during runtime</li>
 *   <li>Persisted to a log file ({@code access_control_audit.log}) for long-term storage</li>
 *   <li>Formatted in a human-readable format with timestamps, event types, and relevant details</li>
 * </ul>
 * </p>
 * 
 * <h3>Usage Example</h3>
 * <pre>
 *   // Get the singleton instance
 *   AuditLogger logger = AuditLoggerSingleton.getInstance();
 *   
 *   // Log an access attempt
 *   logger.logAccessAttempt("CARD-001", "MAIN-ENTRANCE", true, LocalDateTime.now());
 *   
 *   // Retrieve access history for a card
 *   List&lt;AuditRecord&gt; history = logger.getAccessHistory("CARD-001");
 * </pre>
 *
 * @pattern Singleton Pattern - Ensures a class has only one instance and provides
 *          a global point of access to it. This pattern is used here to maintain
 *          a single audit trail across the entire system.
 * @version 1.0
 * @author Access Control System Team
 * @see AuditLogger
 * @see AuditRecord
 * @see AuditEventType
 */
public class AuditLoggerSingleton implements AuditLogger {
    
    private static AuditLoggerSingleton instance;
    private final List<AuditRecord> auditTrail;
    private final String logFilePath = "access_control_audit.log";
    private static final DateTimeFormatter LOG_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Private constructor to prevent instantiation from outside.
     */
    private AuditLoggerSingleton() {
        auditTrail = new ArrayList<>();
        // Could load existing audit records from persistent storage here
    }
    
    /**
     * Returns the singleton instance of AuditLoggerSingleton.
     * Creates the instance if it doesn't exist yet (lazy initialization).
     * 
     * @return the singleton instance
     */
    public static synchronized AuditLoggerSingleton getInstance() {
        if (instance == null) {
            instance = new AuditLoggerSingleton();
        }
        return instance;
    }
    
    @Override
    public void logAccessAttempt(String cardId, String location, boolean isSuccessful, LocalDateTime timestamp) {
        AuditRecord record = new AuditRecord(
                AuditEventType.ACCESS_ATTEMPT,
                cardId,
                location,
                null, // No user ID for access attempts
                isSuccessful,
                timestamp
        );
        auditTrail.add(record);
        saveToFile(record);
    }
    
    @Override
    public void logCardCreation(String cardId, String createdBy, LocalDateTime timestamp) {
        AuditRecord record = new AuditRecord(
                AuditEventType.CARD_CREATION,
                cardId,
                null, // No location for card creation
                createdBy,
                true, // Card creation is always successful if it happens
                timestamp
        );
        auditTrail.add(record);
        saveToFile(record);
    }
    
    @Override
    public void logCardModification(String cardId, String modifiedBy, String modification, LocalDateTime timestamp) {
        AuditRecord record = new AuditRecord(
                AuditEventType.CARD_MODIFICATION,
                cardId,
                null, // No location for card modification
                modifiedBy,
                true, // Card modification is always successful if it happens
                timestamp
        );
        record.addDetail("modification", modification);
        auditTrail.add(record);
        saveToFile(record);
    }
    
    @Override
    public void logCardRevocation(String cardId, String revokedBy, LocalDateTime timestamp) {
        AuditRecord record = new AuditRecord(
                AuditEventType.CARD_REVOCATION,
                cardId,
                null, // No location for card revocation
                revokedBy,
                true, // Card revocation is always successful if it happens
                timestamp
        );
        auditTrail.add(record);
        saveToFile(record);
    }
    
    @Override
    public List<AuditRecord> getAccessHistory(String cardId) {
        return auditTrail.stream()
                .filter(record -> record.getCardId().equals(cardId))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<AuditRecord> getAccessHistory(String location, LocalDateTime startTime, LocalDateTime endTime) {
        return auditTrail.stream()
                .filter(record -> record.getEventType() == AuditEventType.ACCESS_ATTEMPT)
                .filter(record -> record.getLocation() != null && record.getLocation().equals(location))
                .filter(record -> {
                    LocalDateTime timestamp = record.getTimestamp();
                    return (timestamp.isEqual(startTime) || timestamp.isAfter(startTime)) && 
                           (timestamp.isEqual(endTime) || timestamp.isBefore(endTime));
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Saves an audit record to the log file.
     * This is a private method, demonstrating information hiding.
     * 
     * @param record the record to save
     */
    private void saveToFile(AuditRecord record) {
        try {
            Path path = Paths.get(logFilePath);
            String logEntry = formatLogEntry(record);
            Files.writeString(
                    path, 
                    logEntry + System.lineSeparator(),
                    StandardOpenOption.CREATE, 
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.err.println("Failed to write to audit log: " + e.getMessage());
        }
    }
    
    /**
     * Formats an audit record for the log file.
     * 
     * @param record the record to format
     * @return the formatted log entry
     */
    private String formatLogEntry(AuditRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append(record.getTimestamp().format(LOG_FORMATTER))
          .append(" | ")
          .append(record.getEventType())
          .append(" | ")
          .append("Card: ").append(record.getCardId())
          .append(" | ");
        
        if (record.getLocation() != null) {
            sb.append("Location: ").append(record.getLocation())
              .append(" | ");
        }
        
        if (record.getUserId() != null) {
            sb.append("User: ").append(record.getUserId())
              .append(" | ");
        }
        
        sb.append("Outcome: ").append(record.getOutcome() ? "SUCCESS" : "FAILURE");
        
        record.getAdditionalDetails().forEach((key, value) -> 
            sb.append(" | ").append(key).append(": ").append(value)
        );
        
        return sb.toString();
    }
}
