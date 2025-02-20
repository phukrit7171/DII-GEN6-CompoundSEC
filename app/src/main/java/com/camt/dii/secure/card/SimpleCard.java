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
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(cardId.getBytes());
            // Use first 8 bytes for a shorter, but still unique ID
            return String.format("%02x%02x%02x%02x", 
                hash[0], hash[1], hash[2], hash[3]).toUpperCase();
        } catch (Exception e) {
            // Fallback to simple hash if crypto fails
            return String.valueOf(Math.abs(cardId.hashCode()));
        }
    }
}