package com.camt.dii.secure.management;

import com.camt.dii.secure.card.Card;

import java.util.HashMap;
import java.util.Map;

public class SimpleCardManagementService implements CardManagementService {
    private Map<String, Card> cards = new HashMap<>();

    @Override
    public void addCard(Card card) {
        cards.put(card.getFacadeId(), card);
    }

    @Override
    public void revokeCard(String cardId) {
        Card card = findByFacadeId(cardId);
        if (card != null) card.revoke();
    }

    @Override
    public Card findByFacadeId(String facadeId) {
        return cards.get(facadeId);
    }
}