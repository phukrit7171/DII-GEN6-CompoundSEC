package com.securitysystem.management.card;

import com.securitysystem.core.Card;
import com.securitysystem.core.Floor;
import com.securitysystem.core.Room;

import java.util.List;

public class DefaultCardManager implements CardManager {

    private CardStorage cardStorage; // Dependency on CardStorage

    public DefaultCardManager(CardStorage cardStorage) {
        this.cardStorage = cardStorage;
    }

    @Override
    public Card generateNewCard() {
        // Implementation to generate a new card will be added
        return null; // Placeholder
    }

    @Override
    public void addCard(Card card) {
        cardStorage.saveCard(card); // Use CardStorage to persist
    }

    @Override
    public Card getCardById(String cardId) {
        return cardStorage.loadCard(cardId); // Use CardStorage to retrieve
    }

    @Override
    public void modifyCardPermissions(String cardId, List<Floor> floorPermissions, List<Room> roomPermissions) {
        // Implementation to modify permissions will be added
    }

    @Override
    public void revokeCard(String cardId) {
        // Implementation to revoke card will be added
    }
}
