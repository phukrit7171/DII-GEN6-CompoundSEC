// com/camt/dii/secure/management/SimpleCardManagementService.java
package com.camt.dii.secure.management;

import com.camt.dii.secure.card.Card;
import com.camt.dii.secure.card.Permission;
import java.util.HashMap;
import java.util.Map;

public class SimpleCardManagementService implements CardManagementService {

    private final Map<String, Card> cardStore = new HashMap<>(); // In-memory card store for simplicity

    @Override
    public void addCard(Card card) {
        cardStore.put(card.getCardId(), card);
        System.out.println("Card added: " + card.getCardId()); // Simple confirmation
    }

    @Override
    public void modifyCardPermissions(Card card, Permission newPermissions) {
        if (cardStore.containsKey(card.getCardId())) {
            card.setPermissions(newPermissions);
            System.out.println("Permissions modified for card: " + card.getCardId());
        } else {
            System.out.println("Card not found: " + card.getCardId());
        }
    }

    @Override
    public void revokeCard(String cardId) {
        Card card = cardStore.get(cardId);
        if (card != null) {
            card.revoke();
            System.out.println("Card revoked: " + cardId);
        } else {
            System.out.println("Card not found: " + cardId);
        }
    }

    @Override
    public Card findCardByFacadeId(String facadeId) {
        // Find card by matching facade ID
        for (Card card : cardStore.values()) {
            if (card.getFacadeId().equals(facadeId)) {
                return card;
            }
        }
        return null; // No card found with matching facade ID
    }
}