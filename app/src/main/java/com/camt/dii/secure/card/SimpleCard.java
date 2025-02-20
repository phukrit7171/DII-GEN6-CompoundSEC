package com.camt.dii.secure.card;

import com.camt.dii.secure.card.Permission.Permission;

public class SimpleCard implements Card {
    private final String cardId;
    private final String facadeId;
    private boolean revoked;
    private Permission permissions;

    public SimpleCard(String cardId, Permission permissions) {
        this.cardId = cardId;
        this.facadeId = generateFacadeId(cardId);  // Call to AbstractCard's method
        this.revoked = false;
        this.permissions = permissions;
    }

    @Override
    public String getCardId() { return cardId; }
    @Override
    public String getFacadeId() { return facadeId; }
    @Override
    public boolean isValid() { return !revoked; }
    @Override
    public Permission getPermissions() { return permissions; }
    @Override
    public boolean isRevoked() { return revoked; }
    @Override
    public void revoke() { revoked = true; }

    private String generateFacadeId(String cardId) {
        try {
            // Use SHA-256 for secure hashing
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            // Add a salt to prevent rainbow table attacks
            String salt = java.util.UUID.randomUUID().toString();
            byte[] hash = md.digest((salt + cardId).getBytes());
            
            // Convert to hex string, use first 12 characters for readability
            StringBuilder hex = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                hex.append(String.format("%02X", hash[i]));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate facade ID", e);
        }
    }
}