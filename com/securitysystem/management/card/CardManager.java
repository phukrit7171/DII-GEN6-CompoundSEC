package com.securitysystem.management.card;

import com.securitysystem.core.Card;
import com.securitysystem.core.Floor;
import com.securitysystem.core.Room;

import java.util.List;

public interface CardManager {
    Card generateNewCard();
    void addCard(Card card);
    Card getCardById(String cardId);
    void modifyCardPermissions(String cardId, List<Floor> floorPermissions, List<Room> roomPermissions);
    void revokeCard(String cardId);
}
