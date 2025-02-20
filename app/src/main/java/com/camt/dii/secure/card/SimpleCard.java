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
        // Simulate hashing (use actual SHA-256 in real code)
        return cardId.hashCode() + "";  // Placeholder
    }
}