package com.camt.dii.secure.card;

import java.time.LocalDateTime;

import com.camt.dii.secure.access.Floor;

/**
 * Represents access rights for a card. 
 * Defines methods to check access to floors, rooms, and time validity.
 * 
 * This interface demonstrates:
 * 1. Abstraction - Defines what to check (floor, room, time), not how it's managed
 * 2. Encapsulation - Encapsulates access right checks in a well-defined contract
 * 3. Polymorphism - Different permission types can be used interchangeably
 */
public interface Permission {
    
    /**
     * Checks if this permission grants access to the specified floor.
     * 
     * @param floor the floor to check access for
     * @return true if access is granted, false otherwise
     */
    boolean canAccessFloor(Floor floor);
    
    /**
     * Checks if this permission grants access to the specified room.
     * 
     * @param room the room identifier to check access for
     * @return true if access is granted, false otherwise
     */
    boolean canAccessRoom(String room);
    
    /**
     * Checks if this permission is valid at the specified time.
     * 
     * @param time the time to check validity for
     * @return true if permission is valid at the specified time, false otherwise
     */
    boolean isValidForTime(LocalDateTime time);
}
