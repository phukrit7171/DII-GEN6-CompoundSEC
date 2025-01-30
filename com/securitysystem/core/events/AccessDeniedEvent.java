package com.securitysystem.core.events;

import com.securitysystem.core.Card;
import com.securitysystem.core.Location;

public class AccessDeniedEvent extends CardEvent { // Extends CardEvent

    private Location deniedLocation;
    private String reason;
    private long timestamp;

    public AccessDeniedEvent(Card card, Location deniedLocation, String reason) {
        super(card); // Call to superclass constructor
        this.deniedLocation = deniedLocation;
        this.reason = reason;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters will be added later
}
