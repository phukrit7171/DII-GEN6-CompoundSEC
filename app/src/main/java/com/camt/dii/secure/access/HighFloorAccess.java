package com.camt.dii.secure.access;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import com.camt.dii.secure.card.AccessCard;

/**
 * High security floor access with advanced validation.
 * Implements the most stringent security measures for high-security floors.
 */
public class HighFloorAccess implements FloorAccessPolicy {
    
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final Set<DayOfWeek> allowedDays;
    private final int maxDailyAccesses;
    private final Set<String> accessLog = new HashSet<>();
    
    /**
     * Creates a new HighFloorAccess policy with default settings:
     * - Working hours (9:00 AM to 5:00 PM)
     * - Weekdays only (Monday to Friday)
     * - Maximum 5 accesses per day per card
     */
    public HighFloorAccess() {
        this.startTime = LocalTime.of(9, 0); // 9:00 AM
        this.endTime = LocalTime.of(17, 0);  // 5:00 PM
        this.allowedDays = new HashSet<>();
        this.allowedDays.add(DayOfWeek.MONDAY);
        this.allowedDays.add(DayOfWeek.TUESDAY);
        this.allowedDays.add(DayOfWeek.WEDNESDAY);
        this.allowedDays.add(DayOfWeek.THURSDAY);
        this.allowedDays.add(DayOfWeek.FRIDAY);
        this.maxDailyAccesses = 5;
    }
    
    /**
     * Creates a new HighFloorAccess policy with custom settings.
     * 
     * @param startTime the start of the allowed access period
     * @param endTime the end of the allowed access period
     * @param allowedDays set of days when access is allowed
     * @param maxDailyAccesses maximum number of accesses per day per card
     */
    public HighFloorAccess(LocalTime startTime, LocalTime endTime, 
                          Set<DayOfWeek> allowedDays, int maxDailyAccesses) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.allowedDays = new HashSet<>(allowedDays);
        this.maxDailyAccesses = maxDailyAccesses;
    }
    
    @Override
    public boolean validateAccess(AccessCard card, Floor floor, LocalDateTime accessTime) {
        // Check basic access rights
        if (!card.isActive() || !card.hasPermission(floor)) {
            return false;
        }
        
        // Check that the floor is HIGH security (this is specifically for high floors)
        if (floor != Floor.HIGH) {
            return false;
        }
        
        // Check time constraints
        LocalTime time = accessTime.toLocalTime();
        if (time.isBefore(startTime) || time.isAfter(endTime)) {
            return false;
        }
        
        // Check day constraints
        DayOfWeek day = accessTime.getDayOfWeek();
        if (!allowedDays.contains(day)) {
            return false;
        }
        
        // Check daily access limit
        String accessKey = card.getCardId() + "_" + accessTime.toLocalDate();
        int accessCount = (int) accessLog.stream()
                .filter(key -> key.startsWith(card.getCardId() + "_" + accessTime.toLocalDate()))
                .count();
        
        if (accessCount >= maxDailyAccesses) {
            return false;
        }
        
        // Log this access
        accessLog.add(accessKey + "_" + accessTime.toLocalTime());
        
        // All checks passed
        return true;
    }
}
