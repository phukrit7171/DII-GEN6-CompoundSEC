package com.camt.dii.secure.audit.decorator;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.camt.dii.secure.audit.AuditEventType;
import com.camt.dii.secure.audit.AuditLogger;
import com.camt.dii.secure.audit.AuditLoggerSingleton;
import com.camt.dii.secure.audit.AuditRecord;

/**
 * Tests for the DetailedAuditLogDecorator class.
 * Focuses on testing the Decorator pattern implementation and
 * the enhanced functionality provided by the decorator.
 */
public class DetailedAuditLogDecoratorTest {
    
    private AuditLogger baseLogger;
    private AuditLogger decoratedLogger;
    private String testCardId;
    
    @BeforeEach
    void setUp() {
        baseLogger = AuditLoggerSingleton.getInstance();
        decoratedLogger = new DetailedAuditLogDecorator(baseLogger);
        testCardId = "TEST-DEC-" + System.currentTimeMillis(); // Unique ID for each test run
    }
    
    @Test
    void testDecoratorPassesThroughBaseLogging() {
        // Test data
        String location = "TEST-ROOM";
        boolean success = true;
        LocalDateTime timestamp = LocalDateTime.now();
        
        // Log using the decorated logger
        decoratedLogger.logAccessAttempt(testCardId, location, success, timestamp);
        
        // Retrieve access history using the base logger
        List<AuditRecord> history = baseLogger.getAccessHistory(testCardId);
        
        // Verify history contains the record
        assertFalse(history.isEmpty(), "Base logger should receive the log entry from decorator");
        
        // Verify the record details
        AuditRecord record = findRecord(history, AuditEventType.ACCESS_ATTEMPT);
        assertNotNull(record, "Should find the access attempt record");
        assertEquals(testCardId, record.getCardId(), "Card ID should match");
        assertEquals(location, record.getLocation(), "Location should match");
        assertEquals(success, record.getOutcome(), "Success flag should match");
    }
    
    @Test
    void testDecoratorAddsExtraDetails() {
        // Test data
        String userId = "TEST-ADMIN";
        String modification = "Added access to room 301";
        LocalDateTime timestamp = LocalDateTime.now();
        
        // Log a card modification using the decorated logger
        decoratedLogger.logCardModification(testCardId, userId, modification, timestamp);
        
        // Retrieve access history
        List<AuditRecord> history = baseLogger.getAccessHistory(testCardId);
        
        // Find the record
        AuditRecord record = findRecord(history, AuditEventType.CARD_MODIFICATION);
        assertNotNull(record, "Should find the card modification record");
        
        // Verify basic details
        assertEquals(testCardId, record.getCardId(), "Card ID should match");
        assertEquals(userId, record.getUserId(), "User ID should match");
        
        // Check for additional details added by the decorator
        // Note: The exact details depend on implementation, but there should be some
        boolean hasExtraDetails = !record.getAdditionalDetails().isEmpty();
        assertTrue(hasExtraDetails, "Decorated logger should add extra details");
        
        // Specific details we expect to find based on the implementation
        if (!record.getAdditionalDetails().containsKey("modification_details")) {
            System.out.println("Missing key: modification_details");
            System.out.println("Available keys: " + record.getAdditionalDetails().keySet());
            assertTrue(false, "Should find modification_details key");
        }
        if (!record.getAdditionalDetails().containsKey("detailed_timestamp")) {
            System.out.println("Missing key: detailed_timestamp");
            System.out.println("Available keys: " + record.getAdditionalDetails().keySet());
            assertTrue(false, "Should find detailed_timestamp key");
        }
        if (!record.getAdditionalDetails().containsKey("system_info")) {
            System.out.println("Missing key: system_info");
            System.out.println("Available keys: " + record.getAdditionalDetails().keySet());
            assertTrue(false, "Should find system_info key");
        }
    }
    
    @Test
    void testDecoratedCardCreationAndRevocation() {
        // Test data
        String admin = "TEST-SUPERADMIN";
        LocalDateTime timestamp = LocalDateTime.now();
        
        // Log card creation
        decoratedLogger.logCardCreation(testCardId, admin, timestamp);
        
        // Log card revocation with a later timestamp
        LocalDateTime laterTime = timestamp.plusHours(8);
        decoratedLogger.logCardRevocation(testCardId, admin, laterTime);
        
        // Retrieve history
        List<AuditRecord> history = baseLogger.getAccessHistory(testCardId);
        
        // Verify we have both records
        assertEquals(2, history.size(), "Should have creation and revocation records");
        
        // Find both records
        AuditRecord creationRecord = findRecord(history, AuditEventType.CARD_CREATION);
        AuditRecord revocationRecord = findRecord(history, AuditEventType.CARD_REVOCATION);
        
        // Verify records exist
        assertNotNull(creationRecord, "Should find creation record");
        assertNotNull(revocationRecord, "Should find revocation record");
        
        // Check that both have extra details
        assertFalse(creationRecord.getAdditionalDetails().isEmpty(), 
                "Creation record should have extra details");
        assertFalse(revocationRecord.getAdditionalDetails().isEmpty(), 
                "Revocation record should have extra details");
    }
    
    /**
     * Helper method to find a record of a specific type in a list of records.
     * 
     * @param records list of records to search
     * @param type event type to find
     * @return matching record or null if not found
     */
    private AuditRecord findRecord(List<AuditRecord> records, AuditEventType type) {
        for (AuditRecord record : records) {
            if (record.getEventType() == type) {
                return record;
            }
        }
        return null;
    }
}
