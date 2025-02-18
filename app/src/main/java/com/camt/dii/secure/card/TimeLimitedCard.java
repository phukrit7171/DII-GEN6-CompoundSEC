// com/camt/dii/secure/card/TimeLimitedCard.java
package com.camt.dii.secure.card;

import java.time.LocalTime;

public class TimeLimitedCard extends AbstractCard {
    private final LocalTime startTime;
    private final LocalTime endTime;

    public TimeLimitedCard(String cardId, Permission permissions, LocalTime startTime, LocalTime endTime) {
        super(cardId, permissions);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public boolean isValid() {
        // First check the parent class's validity (revocation status, etc.)
        if (!super.isValid()) {
            return false;
        }

        // Check if current time is within the allowed time range
        LocalTime currentTime = LocalTime.now();
        
        // Handle time range that crosses midnight
        if (startTime.isAfter(endTime)) {
            return !currentTime.isBefore(startTime) || !currentTime.isAfter(endTime);
        }
        
        // Normal time range within the same day
        return !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}