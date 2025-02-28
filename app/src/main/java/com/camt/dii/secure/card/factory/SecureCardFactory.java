package com.camt.dii.secure.card.factory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import com.camt.dii.secure.audit.AuditLogger;
import com.camt.dii.secure.audit.AuditLoggerSingleton;
import com.camt.dii.secure.card.AccessCard;
import com.camt.dii.secure.card.CardIdentifier;
import com.camt.dii.secure.card.Permission;

/**
 * Secure implementation of the CardFactory interface.
 * Creates access cards with enhanced security features.
 * 
 * This class demonstrates the Factory Method pattern by providing
 * a concrete implementation that extends the base implementation
 * with additional security features.
 */
public class SecureCardFactory extends StandardCardFactory {
    
    /**
     * Creates a new SecureCardFactory with the default audit logger.
     */
    public SecureCardFactory() {
        super(AuditLoggerSingleton.getInstance());
    }
    
    /**
     * Creates a new SecureCardFactory with a custom audit logger.
     * 
     * @param auditLogger the audit logger to use
     */
    public SecureCardFactory(AuditLogger auditLogger) {
        super(auditLogger);
    }
    
    @Override
    public AccessCard createCard(CardIdentifier identifier, Permission permission) {
        // Generate card ID with enhanced security
        String cardId = generateCardId(identifier);
        cardId = applyEncryption(cardId);
        
        // Create facade IDs with enhanced security
        List<String> facadeIds = initializeFacadeIds(cardId);
        for (int i = 0; i < facadeIds.size(); i++) {
            facadeIds.set(i, applyEncryption(facadeIds.get(i)));
        }
        
        // Log the card creation
        AuditLogger auditLogger = AuditLoggerSingleton.getInstance();
        auditLogger.logCardCreation(cardId, "SYSTEM-SECURE", LocalDateTime.now());
        
        return new AccessCard(cardId, facadeIds, permission);
    }
    
    /**
     * Applies encryption to the specified ID.
     * 
     * @param id the ID to encrypt
     * @return the encrypted ID
     */
    protected String applyEncryption(String id) {
        try {
            // Create SHA-256 hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(id.getBytes(StandardCharsets.UTF_8));
            
            // Encode with Base64
            String base64Hash = Base64.getEncoder().encodeToString(hash);
            
            // Use first 12 characters of hash and append to original ID
            String hashSuffix = base64Hash.substring(0, Math.min(12, base64Hash.length()));
            
            return id + "-" + hashSuffix;
        } catch (NoSuchAlgorithmException e) {
            // Fallback if SHA-256 is not available
            return id + "-SECURE-" + System.currentTimeMillis();
        }
    }
}
