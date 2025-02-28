package com.camt.dii.secure.audit;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Defines the contract for audit logging system.
 * Provides methods for logging various access control events
 * while hiding implementation details.
 * 
 * This interface demonstrates:
 * 1. Abstraction - Defines what to log, not how to log it
 * 2. Information Hiding - Conceals how audit logs are stored and processed
 */
public interface AuditLogger {
    
    /**
     * Logs an access attempt.
     * 
     * @param cardId ID of the card used in the access attempt
     * @param location location of the access attempt (floor or room)
     * @param isSuccessful whether the access attempt was successful
     * @param timestamp when the access attempt occurred
     */
    void logAccessAttempt(String cardId, String location, boolean isSuccessful, LocalDateTime timestamp);
    
    /**
     * Logs a card creation event.
     * 
     * @param cardId ID of the created card
     * @param createdBy ID of the user who created the card
     * @param timestamp when the card was created
     */
    void logCardCreation(String cardId, String createdBy, LocalDateTime timestamp);
    
    /**
     * Logs a card modification event.
     * 
     * @param cardId ID of the modified card
     * @param modifiedBy ID of the user who modified the card
     * @param modification description of the modification
     * @param timestamp when the card was modified
     */
    void logCardModification(String cardId, String modifiedBy, String modification, LocalDateTime timestamp);
    
    /**
     * Logs a card revocation event.
     * 
     * @param cardId ID of the revoked card
     * @param revokedBy ID of the user who revoked the card
     * @param timestamp when the card was revoked
     */
    void logCardRevocation(String cardId, String revokedBy, LocalDateTime timestamp);
    
    /**
     * Retrieves access history for a specific card.
     * 
     * @param cardId ID of the card to retrieve history for
     * @return list of audit records related to the card
     */
    List<AuditRecord> getAccessHistory(String cardId);
    
    /**
     * Retrieves access history for a location in a time range.
     * 
     * @param location location to retrieve history for
     * @param startTime start of the time range
     * @param endTime end of the time range
     * @return list of audit records for the location in the time range
     */
    List<AuditRecord> getAccessHistory(String location, LocalDateTime startTime, LocalDateTime endTime);
}
