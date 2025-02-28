package com.camt.dii.secure.service;

import java.util.Set;

import com.camt.dii.secure.card.AccessCard;

/**
 * Specialized interface for room access control services.
 * Extends the base AccessControlService interface with room-specific functionality.
 * 
 * This interface demonstrates interface inheritance by extending the
 * base interface with more specialized methods.
 */
public interface RoomAccessControlService extends AccessControlService {
    
    /**
     * Gets the set of rooms that the specified card can access.
     * 
     * @param card the access card to check
     * @return set of room IDs that the card can access
     */
    Set<String> getAccessibleRooms(AccessCard card);
    
    /**
     * Checks if the specified card can access the specified room.
     * 
     * @param card the access card to check
     * @param roomId the room ID to check access for
     * @return true if the card can access the room, false otherwise
     */
    boolean canAccessRoom(AccessCard card, String roomId);
    
    /**
     * Checks if the specified card can access the specified room on the specified floor.
     * This method should verify that the card has access to both the floor and the room.
     * 
     * @param card the access card to check
     * @param floorId the floor ID where the room is located
     * @param roomId the room ID to check access for
     * @return true if the card can access the room, false otherwise
     */
    boolean canAccessRoom(AccessCard card, String floorId, String roomId);
}
