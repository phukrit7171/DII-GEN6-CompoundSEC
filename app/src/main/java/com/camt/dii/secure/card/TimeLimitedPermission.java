package com.camt.dii.secure.card;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.camt.dii.secure.access.Floor;

/**
 * A time-limited implementation of Permission that grants access based on
 * a set of allowed floors and rooms, but only during a specific time period.
 * 
 * This class demonstrates the polymorphism principle by providing a different
 * implementation of the Permission interface with time-based constraints.
 */
public class TimeLimitedPermission implements Permission {
    
    private final Set<Floor> allowedFloors;
    private final Set<String> allowedRooms;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    
    /**
     * Creates a new TimeLimitedPermission with the specified allowed floors, rooms,
     * and validity period.
     * 
     * @param allowedFloors set of floors that can be accessed
     * @param allowedRooms set of room IDs that can be accessed
     * @param startTime the start of the validity period (inclusive)
     * @param endTime the end of the validity period (inclusive)
     */
    public TimeLimitedPermission(Set<Floor> allowedFloors, Set<String> allowedRooms, 
                                LocalDateTime startTime, LocalDateTime endTime) {
        this.allowedFloors = Collections.unmodifiableSet(new HashSet<>(allowedFloors));
        this.allowedRooms = Collections.unmodifiableSet(new HashSet<>(allowedRooms));
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    @Override
    public boolean canAccessFloor(Floor floor) {
        return allowedFloors.contains(floor);
    }
    
    @Override
    public boolean canAccessRoom(String room) {
        return allowedRooms.contains(room);
    }
    
    @Override
    public boolean isValidForTime(LocalDateTime time) {
        return (time.isEqual(startTime) || time.isAfter(startTime)) && 
               (time.isEqual(endTime) || time.isBefore(endTime));
    }
    
    /**
     * Returns an unmodifiable view of the allowed floors.
     * 
     * @return set of allowed floors
     */
    public Set<Floor> getAllowedFloors() {
        return allowedFloors;
    }
    
    /**
     * Returns an unmodifiable view of the allowed rooms.
     * 
     * @return set of allowed rooms
     */
    public Set<String> getAllowedRooms() {
        return allowedRooms;
    }
    
    /**
     * Returns the start time of the validity period.
     * 
     * @return start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    /**
     * Returns the end time of the validity period.
     * 
     * @return end time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }
}
