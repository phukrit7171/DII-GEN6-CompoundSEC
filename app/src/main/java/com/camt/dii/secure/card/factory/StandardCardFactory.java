package com.camt.dii.secure.card.factory;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.camt.dii.secure.audit.AuditLogger;
import com.camt.dii.secure.audit.AuditLoggerSingleton;
import com.camt.dii.secure.card.AccessCard;
import com.camt.dii.secure.card.CardIdentifier;
import com.camt.dii.secure.card.Permission;

/**
 * Standard implementation of the CardFactory interface.
 * Creates standard access cards with basic security features.
 * 
 * This class demonstrates the Factory Method pattern by providing
 * a concrete implementation of the createCard factory method.
 */
public class StandardCardFactory implements CardFactory {
    
    private final AuditLogger auditLogger;
    
    /**
     * Creates a new StandardCardFactory with the default audit logger.
     */
    public StandardCardFactory() {
        this.auditLogger = AuditLoggerSingleton.getInstance();
    }
    
    /**
     * Creates a new StandardCardFactory with a custom audit logger.
     * Useful for testing and dependency injection.
     * 
     * @param auditLogger the audit logger to use
     */
    public StandardCardFactory(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
    }
    
    @Override
    public AccessCard createCard(CardIdentifier identifier, Permission permission) {
        String cardId = generateCardId(identifier);
        List<String> facadeIds = initializeFacadeIds(cardId);
        
        // Log the card creation
        auditLogger.logCardCreation(cardId, "SYSTEM", identifier.getIssueDate());
        
        return new AccessCard(cardId, facadeIds, permission);
    }
    
    /**
     * Generates a card ID from the card identifier.
     * 
     * @param identifier the card identifier
     * @return the generated card ID
     */
    protected String generateCardId(CardIdentifier identifier) {
        String serialPart = identifier.getSerialNumber();
        String issuerPart = identifier.getIssuerId().substring(0, Math.min(3, identifier.getIssuerId().length()));
        String datePart = identifier.getIssueDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        
        return issuerPart.toUpperCase() + "-" + serialPart + "-" + datePart;
    }
    
    /**
     * Initializes the list of facade IDs for the card.
     * 
     * @param cardId the card ID
     * @return the list of facade IDs
     */
    protected List<String> initializeFacadeIds(String cardId) {
        List<String> facadeIds = new ArrayList<>();
        
        // Generate 3 facade IDs for the card
        for (int i = 0; i < 3; i++) {
            facadeIds.add(cardId + "-FACADE-" + UUID.randomUUID().toString().substring(0, 8));
        }
        
        return facadeIds;
    }
}
