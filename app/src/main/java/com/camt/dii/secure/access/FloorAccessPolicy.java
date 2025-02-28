package com.camt.dii.secure.access;

import java.time.LocalDateTime;

import com.camt.dii.secure.card.AccessCard;

/**
 * Defines the contract for floor access policies.
 * Different implementations can provide different validation rules.
 * 
 * This interface demonstrates polymorphism by defining a contract
 * that multiple implementations can fulfill in different ways.
 */
public interface FloorAccessPolicy {
    
    /**
     * Validates whether the specified card can access the specified floor at the specified time.
     * 
     * @param card the access card attempting to gain access
     * @param floor the floor being accessed
     * @param accessTime the time of the access attempt
     * @return true if access is granted, false otherwise
     */
    boolean validateAccess(AccessCard card, Floor floor, LocalDateTime accessTime);
}
