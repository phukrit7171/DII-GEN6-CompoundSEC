package com.camt.dii.secure.audit.decorator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.camt.dii.secure.audit.AuditEventType;
import com.camt.dii.secure.audit.AuditLogger;
import com.camt.dii.secure.audit.AuditRecord;

/**
 * Concrete decorator that adds detailed logging functionality to any AuditLogger implementation.
 * <p>
 * This class demonstrates the Decorator pattern by extending the abstract decorator
 * ({@link AuditLogServiceDecorator}) and enhancing the behavior of the wrapped
 * {@link AuditLogger}'s methods. It adds extensive system information and additional
 * context to each audit record without modifying the original AuditLogger implementation.
 * </p>
 * 
 * <h3>Decorator Pattern Implementation</h3>
 * <p>
 * The Decorator pattern is implemented with these components:
 * <ol>
 *   <li><b>Component Interface</b> - {@link AuditLogger} defines the common interface</li>
 *   <li><b>Abstract Decorator</b> - {@link AuditLogServiceDecorator} forwards operations to the wrapped component</li>
 *   <li><b>Concrete Decorator</b> - This class adds behavior before/after forwarding operations</li>
 * </ol>
 * </p>
 * 
 * <h3>Additional Information Added</h3>
 * <p>
 * This decorator enriches audit records with:
 * <ul>
 *   <li>System information (OS name, version, Java version)</li>
 *   <li>Application version details</li>
 *   <li>ISO-formatted timestamp for precise time tracking</li>
 *   <li>Thread information for concurrent execution analysis</li>
 *   <li>Memory usage statistics</li>
 * </ul>
 * </p>
 * 
 * <h3>Usage Example</h3>
 * <pre>
 *   // Create a base logger (the component being decorated)
 *   AuditLogger baseLogger = AuditLoggerSingleton.getInstance();
 *   
 *   // Create a decorated logger with enhanced functionality
 *   AuditLogger detailedLogger = new DetailedAuditLogDecorator(baseLogger);
 *   
 *   // Use the decorated logger - the additional details are added automatically
 *   detailedLogger.logAccessAttempt("CARD-001", "MAIN-ENTRANCE", true, LocalDateTime.now());
 * </pre>
 *
 * @pattern Decorator Pattern - Attaches additional responsibilities to an object dynamically.
 *          Decorators provide a flexible alternative to subclassing for extending functionality.
 * @version 1.0
 * @author Access Control System Team
 * @see AuditLogServiceDecorator
 * @see AuditLogger
 */
public class DetailedAuditLogDecorator extends AuditLogServiceDecorator {
    
    /**
     * Creates a new DetailedAuditLogDecorator wrapping the specified AuditLogger.
     * 
     * @param logger the AuditLogger to wrap
     */
    public DetailedAuditLogDecorator(AuditLogger logger) {
        super(logger);
    }
    
    @Override
    public void logAccessAttempt(String cardId, String location, boolean isSuccessful, LocalDateTime timestamp) {
        // Create a detailed record with extra information
        AuditRecord detailedRecord = new AuditRecord(
                AuditEventType.ACCESS_ATTEMPT,
                cardId,
                location,
                null, // No user ID for access attempts
                isSuccessful,
                timestamp
        );
        
        // Add extra details
        addExtraDetails(detailedRecord);
        
        // Log with the base logger
        super.logAccessAttempt(cardId, location, isSuccessful, timestamp);
    }
    
    @Override
    public void logCardCreation(String cardId, String createdBy, LocalDateTime timestamp) {
        // Create a detailed record with extra information
        AuditRecord detailedRecord = new AuditRecord(
                AuditEventType.CARD_CREATION,
                cardId,
                null, // No location for card creation
                createdBy,
                true, // Card creation is always successful if it happens
                timestamp
        );
        
        // Add extra details
        addExtraDetails(detailedRecord);
        
        // Log with the base logger
        super.logCardCreation(cardId, createdBy, timestamp);
    }
    
    @Override
    public void logCardModification(String cardId, String modifiedBy, String modification, LocalDateTime timestamp) {
        // Create a detailed record with extra information
        AuditRecord detailedRecord = new AuditRecord(
                AuditEventType.CARD_MODIFICATION,
                cardId,
                null, // No location for card modification
                modifiedBy,
                true, // Card modification is always successful if it happens
                timestamp
        );
        
        // Add extra details
        addExtraDetails(detailedRecord);
        detailedRecord.addDetail("modification_details", modification);
        
        // Log with the base logger
        super.logCardModification(cardId, modifiedBy, modification, timestamp);
    }
    
    @Override
    public void logCardRevocation(String cardId, String revokedBy, LocalDateTime timestamp) {
        // Create a detailed record with extra information
        AuditRecord detailedRecord = new AuditRecord(
                AuditEventType.CARD_REVOCATION,
                cardId,
                null, // No location for card revocation
                revokedBy,
                true, // Card revocation is always successful if it happens
                timestamp
        );
        
        // Add extra details
        addExtraDetails(detailedRecord);
        
        // Log with the base logger
        super.logCardRevocation(cardId, revokedBy, timestamp);
    }
    
    /**
     * Adds extra system and environment details to an audit record.
     * This is a private helper method demonstrating the decorator pattern's
     * ability to add behavior.
     * 
     * @param record the record to add details to
     */
    private void addExtraDetails(AuditRecord record) {
        // Add system information
        record.addDetail("system_info", System.getProperty("os.name"));
        record.addDetail("system_version", System.getProperty("os.version"));
        record.addDetail("java_version", System.getProperty("java.version"));
        
        // Add application information
        record.addDetail("application_version", "1.0.0");
        
        // Add detailed timestamp
        record.addDetail("detailed_timestamp", 
                record.getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME));
        
        // Add thread information
        record.addDetail("thread_name", Thread.currentThread().getName());
        
        // Add memory information
        Runtime runtime = Runtime.getRuntime();
        long memory = runtime.totalMemory() - runtime.freeMemory();
        record.addDetail("memory_usage_mb", String.valueOf(memory / (1024 * 1024)));
    }
}
