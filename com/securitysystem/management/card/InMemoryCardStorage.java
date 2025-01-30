package com.securitysystem.management.card;

import com.securitysystem.core.Card;

import java.util.HashMap;
import java.util.Map;

public class InMemoryCardStorage implements CardStorage {

    private Map<String, Card> cardMap = new HashMap<>();

    @Override
    public void saveCard(Card card) {
        cardMap.put(card.getCardId(), card);
    }

    @Override
    public Card loadCard(String cardId) {
        return cardMap.get(cardId);
    }

    @Override
    public void updateCard(Card card) {
        cardMap.put(card.getCardId(), card);
    }

    @Override
    public void deleteCard(String cardId) {
        cardMap.remove(cardId);
    }
}
