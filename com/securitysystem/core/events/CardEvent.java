package com.securitysystem.core.events;

/**
 * Abstract base class for all card-related events.
 */
public abstract class CardEvent {
    private Card card;
    private long eventTime;

    public CardEvent(Card card) {
        this.card = card;
        this.eventTime = System.currentTimeMillis();
    }

    public Card getCard() {
        return card;
    }

    public long getEventTime() {
        return eventTime;
    }
}
