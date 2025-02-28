package com.camt.dii.secure.audit;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Tests for the AuditLoggerSingleton class.
 * Focuses on testing the Singleton pattern implementation and basic logging functionality.
 */
public class AuditLoggerSingletonTest {
    
    @Test
    void testSingletonPattern() {
        // Get two instances of the singleton
        AuditLogger instance1 = AuditLoggerSingleton.getInstance();
        AuditLogger instance2 = AuditLoggerSingleton.getInstance();
        
        // Verify not null
        assertNotNull(instance1, "Singleton instance should not be null");
        
        // Verify same instance is returned
        assertSame(instance1, instance2, "Multiple calls to getInstance should return the same instance");
    }
    
    @Test
    void testAccessLogging() {
        // Get the singleton instance
        AuditLogger logger = AuditLoggerSingleton.getInstance();
        
        // Test data
        String cardId = "TEST-CARD-001";
        String location = "TEST-LOCATION";
        boolean success = true;
        LocalDateTime timestamp = LocalDateTime.now();
        
        // Log an access attempt
        logger.logAccessAttempt(cardId, location, success, timestamp);
        
        // Retrieve access history for the card
        List<AuditRecord> history = logger.getAccessHistory(cardId);
        
        // Verify history is not empty
        assertTrue(history.size() > 0, "Access history should contain at least one record");
        
        // Find our test record
        boolean foundRecord = false;
        for (AuditRecord record : history) {
            if (record.getCardId().equals(cardId) && 
                record.getLocation().equals(location) && 
                record.getOutcome() == success &&
                record.getEventType() == AuditEventType.ACCESS_ATTEMPT) {
                foundRecord = true;
                break;
            }
        }
        
        assertTrue(foundRecord, "Access attempt should be recorded in access history");
    }
    
    @Test
    void testCardCreationLogging() {
        // Get the singleton instance
        AuditLogger logger = AuditLoggerSingleton.getInstance();
        
        // Test data
        String cardId = "TEST-CARD-002";
        String createdBy = "TEST-ADMIN";
        LocalDateTime timestamp = LocalDateTime.now();
        
        // Log a card creation
        logger.logCardCreation(cardId, createdBy, timestamp);
        
        // Retrieve access history for the card
        List<AuditRecord> history = logger.getAccessHistory(cardId);
        
        // Verify history is not empty
        assertTrue(history.size() > 0, "Card history should contain at least one record");
        
        // Find our test record
        boolean foundRecord = false;
        for (AuditRecord record : history) {
            if (record.getCardId().equals(cardId) && 
                record.getUserId() != null && record.getUserId().equals(createdBy) &&
                record.getEventType() == AuditEventType.CARD_CREATION) {
                foundRecord = true;
                break;
            }
        }
        
        assertTrue(foundRecord, "Card creation should be recorded in access history");
    }
    
    @Test
    void testCardManagementLogging() {
        // Get the singleton instance
        AuditLogger logger = AuditLoggerSingleton.getInstance();
        
        // Test data
        String cardId = "TEST-CARD-003";
        String userId = "TEST-MANAGER";
        String modification = "Added HIGH floor access";
        LocalDateTime timestamp = LocalDateTime.now();
        
        // Log card modification
        logger.logCardModification(cardId, userId, modification, timestamp);
        
        // Log card revocation
        logger.logCardRevocation(cardId, userId, timestamp.plusMinutes(10));
        
        // Retrieve access history for the card
        List<AuditRecord> history = logger.getAccessHistory(cardId);
        
        // Verify history contains both records
        assertEquals(2, history.size(), "Card history should contain two records");
        
        // Verify we have one modification and one revocation
        boolean foundModification = false;
        boolean foundRevocation = false;
        
        for (AuditRecord record : history) {
            if (record.getEventType() == AuditEventType.CARD_MODIFICATION) {
                foundModification = true;
            } else if (record.getEventType() == AuditEventType.CARD_REVOCATION) {
                foundRevocation = true;
            }
        }
        
        assertTrue(foundModification, "Card modification should be recorded");
        assertTrue(foundRevocation, "Card revocation should be recorded");
    }
}
