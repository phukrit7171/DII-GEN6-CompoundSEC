package com.securitysystem.core.validators;

import com.securitysystem.core.Card;
import com.securitysystem.core.Location;
import com.securitysystem.core.events.AccessRequestEvent;

public interface CardValidator {
    boolean isValid(Card card, Location location, AccessRequestEvent event);
}
