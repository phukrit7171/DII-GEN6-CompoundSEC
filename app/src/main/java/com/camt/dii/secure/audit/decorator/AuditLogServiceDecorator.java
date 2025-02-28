package com.camt.dii.secure.audit.decorator;

import java.time.LocalDateTime;
import java.util.List;

import com.camt.dii.secure.audit.AuditLogger;
import com.camt.dii.secure.audit.AuditRecord;

/**
 * Abstract decorator class for AuditLogger implementations.
 * Implements the AuditLogger interface and wraps another AuditLogger instance.
 * 
 * This class demonstrates the Decorator pattern:
 * 1. Implements the same interface as the objects it decorates
 * 2. Delegates operations to the wrapped component
 * 3. Provides a base for adding functionality in concrete decorators
 */
public abstract class AuditLogServiceDecorator implements AuditLogger {
    
    /**
     * The wrapped AuditLogger instance.
     * Protected to allow access in concrete decorator subclasses.
     */
    protected AuditLogger wrappedLogger;
    
    /**
     * Creates a new decorator wrapping the specified AuditLogger.
     * 
     * @param logger the AuditLogger to wrap
     */
    public AuditLogServiceDecorator(AuditLogger logger) {
        this.wrappedLogger = logger;
    }
    
    @Override
    public void logAccessAttempt(String cardId, String location, boolean isSuccessful, LocalDateTime timestamp) {
        wrappedLogger.logAccessAttempt(cardId, location, isSuccessful, timestamp);
    }
    
    @Override
    public void logCardCreation(String cardId, String createdBy, LocalDateTime timestamp) {
        wrappedLogger.logCardCreation(cardId, createdBy, timestamp);
    }
    
    @Override
    public void logCardModification(String cardId, String modifiedBy, String modification, LocalDateTime timestamp) {
        wrappedLogger.logCardModification(cardId, modifiedBy, modification, timestamp);
    }
    
    @Override
    public void logCardRevocation(String cardId, String revokedBy, LocalDateTime timestamp) {
        wrappedLogger.logCardRevocation(cardId, revokedBy, timestamp);
    }
    
    @Override
    public List<AuditRecord> getAccessHistory(String cardId) {
        return wrappedLogger.getAccessHistory(cardId);
    }
    
    @Override
    public List<AuditRecord> getAccessHistory(String location, LocalDateTime startTime, LocalDateTime endTime) {
        return wrappedLogger.getAccessHistory(location, startTime, endTime);
    }
}
