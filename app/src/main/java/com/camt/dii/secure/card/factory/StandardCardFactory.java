package com.camt.dii.secure.card.factory;

import java.time.format.DateTimeFormatter;


import com.camt.dii.secure.audit.AuditLogger;
import com.camt.dii.secure.audit.AuditLoggerSingleton;
import com.camt.dii.secure.card.AccessCard;
import com.camt.dii.secure.card.CardIdentifier;
import com.camt.dii.secure.card.Permission;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        String facadeIds = initializeFacadeIds(cardId);
        
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
    protected String initializeFacadeIds(String cardId) {
        String facadeIds;
        
    
        facadeIds = (hashFacadeId(cardId));

        
        return facadeIds;
    }
    
    /**
     * Hashes the facade ID using SHA-256.
     * 
     * @param facadeId the facade ID to hash
     * @return the hashed facade ID
     */
    private String hashFacadeId(String facadeId) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(facadeId.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}
