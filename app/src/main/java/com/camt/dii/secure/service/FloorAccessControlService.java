package com.camt.dii.secure.service;

import java.util.Set;

import com.camt.dii.secure.access.Floor;
import com.camt.dii.secure.card.AccessCard;

/**
 * Specialized interface for floor access control services.
 * Extends the base AccessControlService interface with floor-specific functionality.
 * 
 * This interface demonstrates interface inheritance by extending the
 * base interface with more specialized methods.
 */
public interface FloorAccessControlService extends AccessControlService {
    
    /**
     * Gets the set of floors that the specified card can access.
     * 
     * @param card the access card to check
     * @return set of floors that the card can access
     */
    Set<Floor> getAccessibleFloors(AccessCard card);
    
    /**
     * Checks if the specified card can access the specified floor.
     * 
     * @param card the access card to check
     * @param floor the floor to check access for
     * @return true if the card can access the floor, false otherwise
     */
    boolean canAccessFloor(AccessCard card, Floor floor);
}
