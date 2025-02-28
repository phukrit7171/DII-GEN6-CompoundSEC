package com.camt.dii.secure.card;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.camt.dii.secure.access.Floor;

/**
 * A simple implementation of Permission that grants access based on a 
 * fixed set of allowed floors and rooms.
 * 
 * This class is always valid for any time (time-independent).
 */
public class SimplePermission implements Permission {
    
    private final Set<Floor> allowedFloors;
    private final Set<String> allowedRooms;
    
    /**
     * Creates a new SimplePermission with the specified allowed floors and rooms.
     * 
     * @param allowedFloors set of floors that can be accessed
     * @param allowedRooms set of room IDs that can be accessed
     */
    public SimplePermission(Set<Floor> allowedFloors, Set<String> allowedRooms) {
        this.allowedFloors = Collections.unmodifiableSet(new HashSet<>(allowedFloors));
        this.allowedRooms = Collections.unmodifiableSet(new HashSet<>(allowedRooms));
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
        // SimplePermission is always valid regardless of time
        return true;
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
}
