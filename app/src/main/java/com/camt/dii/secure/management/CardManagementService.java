// com/camt/dii/secure/management/CardManagementService.java
package com.camt.dii.secure.management;

import com.camt.dii.secure.card.Card;
import com.camt.dii.secure.card.Permission;

public interface CardManagementService {
    void addCard(Card card);
    void modifyCardPermissions(Card card, Permission newPermissions);
    void revokeCard(String cardId);
    
    /**
     * Find a card by its facade ID
     * @param facadeId The facade ID of the card to find
     * @return The card with the matching facade ID, or null if not found
     */
    Card findCardByFacadeId(String facadeId);
}