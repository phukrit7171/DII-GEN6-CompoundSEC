package com.camt.dii.secure.access;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.camt.dii.secure.card.AccessCard;

/**
 * Medium security floor access with time restrictions.
 * Adds time-based restrictions to access validation.
 */
public class MediumFloorAccess implements FloorAccessPolicy {
    
    private final LocalTime startTime;
    private final LocalTime endTime;
    
    /**
     * Creates a new MediumFloorAccess policy with default business hours (8:00 AM to 6:00 PM).
     */
    public MediumFloorAccess() {
        this.startTime = LocalTime.of(8, 0); // 8:00 AM
        this.endTime = LocalTime.of(18, 0);  // 6:00 PM
    }
    
    /**
     * Creates a new MediumFloorAccess policy with the specified access hours.
     * 
     * @param startTime the start of the allowed access period
     * @param endTime the end of the allowed access period
     */
    public MediumFloorAccess(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    @Override
    public boolean validateAccess(AccessCard card, Floor floor, LocalDateTime accessTime) {
        // Check basic access rights
        if (!card.isActive() || !card.hasPermission(floor)) {
            return false;
        }
        
        // Medium floors also check that access is during allowed hours
        LocalTime time = accessTime.toLocalTime();
        return !time.isBefore(startTime) && !time.isAfter(endTime);
    }
}
