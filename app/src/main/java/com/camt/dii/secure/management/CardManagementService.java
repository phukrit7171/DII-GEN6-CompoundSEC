package com.camt.dii.secure.management;

import com.camt.dii.secure.card.Card;

public interface CardManagementService {
    void addCard(Card card);
    void revokeCard(String cardId);
    Card findByFacadeId(String facadeId);
}