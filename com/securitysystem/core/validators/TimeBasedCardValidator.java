package com.securitysystem.core.validators;

import com.securitysystem.core.Card;
import com.securitysystem.core.Location;
import com.securitysystem.core.events.AccessRequestEvent;

public class TimeBasedCardValidator implements CardValidator {

    @Override
    public boolean isValid(Card card, Location location, AccessRequestEvent event) {
        // Time-based card validation logic will be implemented here
        return false; // Placeholder
    }
}
