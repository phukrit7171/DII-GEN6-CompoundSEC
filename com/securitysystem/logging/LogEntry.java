package com.securitysystem.logging;

package com.securitysystem.logging;

import com.securitysystem.core.Card;
import com.securitysystem.core.Location;
import com.securitysystem.core.events.CardEvent;

import java.time.LocalDateTime;

public class LogEntry {

    private LocalDateTime timestamp;
    private Card card;
    private Location location;
    private CardEvent.class eventType; // Could use enum or class type
    private String message;

    public LogEntry(Card card, Location location, CardEvent.class eventType, String message) {
        this.timestamp = LocalDateTime.now();
        this.card = card;
        this.location = location;
        this.eventType = eventType;
        this.message = message;
    }

    // Getters for log entry attributes will be added
}
