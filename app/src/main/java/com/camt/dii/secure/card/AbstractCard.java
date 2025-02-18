// com/camt/dii/secure/card/AbstractCard.java
package com.camt.dii.secure.card;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public abstract class AbstractCard implements Card {
    private final String cardId;
    private final String facadeId;
    private Permission permissions;
    private boolean revoked;

    public AbstractCard(String cardId, Permission permissions) {
        this.cardId = cardId;
        this.facadeId = hashCardId(cardId);
        this.permissions = permissions;
        this.revoked = false;
    }

    @Override
    public String getCardId() {
        return cardId;
    }

    @Override
    public String getFacadeId() {
        return facadeId;
    }

    @Override
    public Permission getPermissions() {
        return permissions;
    }

    @Override
    public void setPermissions(Permission permissions) {
        // In this simplified design, permissions are set only at creation.
        // For modification, CardManagementService would handle it (not implemented in detail here).
        throw new UnsupportedOperationException("Modifying permissions directly on Card is not supported in this simple version.");
    }

    @Override
    public boolean isRevoked() {
        return revoked;
    }

    @Override
    public void revoke() {
        this.revoked = true;
    }

    @Override
    public boolean isValid() {
        return !isRevoked() && getPermissions().isValidForTime(java.time.LocalDateTime.now());
    }

    private String hashCardId(String cardId) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(cardId.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // In a real app, handle this exception more gracefully
        }
    }
}