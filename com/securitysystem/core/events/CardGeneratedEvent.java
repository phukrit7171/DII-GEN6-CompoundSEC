package com.securitysystem.core.events;

import com.securitysystem.core.Card;

public class CardGeneratedEvent extends CardEvent { // Extends CardEvent

    public CardGeneratedEvent(Card card) {
        super(card); // Call to superclass constructor
    }
}
