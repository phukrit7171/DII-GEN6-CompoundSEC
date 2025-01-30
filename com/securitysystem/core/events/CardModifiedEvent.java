package com.securitysystem.core.events;

import com.securitysystem.core.Card;

public class CardModifiedEvent extends CardEvent { // Extends CardEvent

    public CardModifiedEvent(Card card) {
        super(card); // Call to superclass constructor
    }
}
