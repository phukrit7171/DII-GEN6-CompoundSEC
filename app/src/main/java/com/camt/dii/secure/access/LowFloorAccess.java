package com.camt.dii.secure.access;

import java.time.LocalDateTime;

import com.camt.dii.secure.card.AccessCard;

/**
 * Simple access policy for low-security floors.
 * Basic validation with minimal security requirements.
 */
public class LowFloorAccess implements FloorAccessPolicy {
    
    @Override
    public boolean validateAccess(AccessCard card, Floor floor, LocalDateTime accessTime) {
        // For low floors, we only check if the card is active and has permission for the floor
        // We don't do additional validation
        return card.isActive() && card.hasPermission(floor);
    }
}
