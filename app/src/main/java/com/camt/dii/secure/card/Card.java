package com.camt.dii.secure.card;

import com.camt.dii.secure.card.Permission.Permission;

public interface Card {
    String getCardId();
    String getFacadeId();
    boolean isValid();
    Permission getPermissions();
    boolean isRevoked();
    void revoke();
}