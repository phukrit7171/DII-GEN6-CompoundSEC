package com.securitysystem.core.events;

import com.securitysystem.core.Card;

public class CardRevokedEvent extends CardEvent { // Extends CardEvent

    public CardRevokedEvent(Card card) {
        super(card); // Call to superclass constructor
    }
}
