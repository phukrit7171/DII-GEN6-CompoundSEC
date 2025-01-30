package com.securitysystem.core.events;

import com.securitysystem.core.Card;
import com.securitysystem.core.Location;

public class AccessGrantedEvent extends CardEvent { // Extends CardEvent

    private Location grantedLocation;
    private long timestamp;

    public AccessGrantedEvent(Card card, Location grantedLocation) {
        super(card); // Call to superclass constructor
        this.grantedLocation = grantedLocation;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters will be added later
}
