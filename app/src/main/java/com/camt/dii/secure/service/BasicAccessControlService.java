package com.camt.dii.secure.service;

import java.time.LocalDateTime;

import com.camt.dii.secure.audit.AuditLogger;
import com.camt.dii.secure.audit.AuditLoggerSingleton;
import com.camt.dii.secure.card.AccessCard;

/**
 * Basic implementation of the AccessControlService interface.
 * Provides simple access control functionality.
 * 
 * This class demonstrates implementation of an interface and
 * the use of the Singleton pattern for accessing the audit logger.
 */
public class BasicAccessControlService implements AccessControlService {
    
    private final AuditLogger auditLogger;
    
    /**
     * Creates a new BasicAccessControlService with the default audit logger.
     */
    public BasicAccessControlService() {
        this.auditLogger = AuditLoggerSingleton.getInstance();
    }
    
    /**
     * Creates a new BasicAccessControlService with the specified audit logger.
     * Allows for easier testing and customization of logging behavior.
     * 
     * @param auditLogger the audit logger to use
     */
    public BasicAccessControlService(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
    }
    
    @Override
    public boolean validateAccess(AccessCard card, String location, LocalDateTime time) {
        // Basic access validation - check if the card is active and has permission
        // for a location (could be a floor or room)
        boolean granted = card.isActive() && card.getPermission().isValidForTime(time);
        
        // Log the access attempt
        logAccessAttempt(card, location, granted, time);
        
        return granted;
    }
    
    @Override
    public void logAccessAttempt(AccessCard card, String location, boolean granted, LocalDateTime time) {
        auditLogger.logAccessAttempt(card.getCardId(), location, granted, time);
    }
}
