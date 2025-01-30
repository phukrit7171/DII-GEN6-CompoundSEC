package com.securitysystem.management.card;

import com.securitysystem.core.Card;

public class DatabaseCardStorage implements CardStorage {

    @Override
    public void saveCard(Card card) {
        // Implementation to save card to a database will be added
    }

    @Override
    public Card loadCard(String cardId) {
        // Implementation to load card from a database will be added
        return null; // Placeholder
    }

    @Override
    public void updateCard(Card card) {
        // Implementation to update card in a database will be added
    }

    @Override
    public void deleteCard(String cardId) {
        // Implementation to delete card from a database will be added
    }
}
