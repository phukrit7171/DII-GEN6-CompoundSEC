package com.securitysystem.management.card;

import com.securitysystem.core.Card;

public interface CardStorage {
    void saveCard(Card card);
    Card loadCard(String cardId);
    void updateCard(Card card);
    void deleteCard(String cardId);
    // ... other storage operations if needed ...
}
