package com.securitysystem.core.events;

import com.securitysystem.core.Card;
import com.securitysystem.core.Location;

public class AccessRequestEvent extends CardEvent { // Extends CardEvent

    private Location requestedLocation;
    private long timestamp;

    public AccessRequestEvent(Card card, Location requestedLocation) {
        super(card); // Call to superclass constructor
        this.requestedLocation = requestedLocation;
        this.timestamp = System.currentTimeMillis(); // Record timestamp
    }

    // Getters for location, timestamp, etc. will be added later
}
