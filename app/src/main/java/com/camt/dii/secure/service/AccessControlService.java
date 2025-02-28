package com.camt.dii.secure.service;

import java.time.LocalDateTime;

import com.camt.dii.secure.card.AccessCard;

/**
 * Base interface for all access control services.
 * Defines the common operations that all access control services must support.
 * 
 * This interface demonstrates abstraction by defining what operations
 * access control services perform without specifying how they are implemented.
 */
public interface AccessControlService {
    
    /**
     * Validates whether the specified card can access the specified location at the specified time.
     * 
     * @param card the access card attempting to gain access
     * @param location the location being accessed (floor or room identifier)
     * @param time the time of the access attempt
     * @return true if access is granted, false otherwise
     */
    boolean validateAccess(AccessCard card, String location, LocalDateTime time);
    
    /**
     * Logs an access attempt.
     * 
     * @param card the access card used in the attempt
     * @param location the location where access was attempted
     * @param granted whether access was granted
     * @param time the time of the access attempt
     */
    void logAccessAttempt(AccessCard card, String location, boolean granted, LocalDateTime time);
}
