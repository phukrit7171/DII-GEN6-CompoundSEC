package com.camt.dii.secure.card;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.camt.dii.secure.access.Floor;

/**
 * Represents an access card with time-based encryption and multiple façade IDs.
 * Encapsulates all card data and provides controlled access to it.
 * 
 * This class demonstrates encapsulation and information hiding principles.
 */
public class AccessCard {
    
    private final String cardId;
    private final List<String> facadeIds;
    private final Permission permission;
    private boolean isActive;
    private final LocalDateTime creationDate;
    private LocalDateTime lastUsed;
    
    /**
     * Creates a new AccessCard with the provided details.
     * 
     * @param cardId unique identifier for the card
     * @param facadeIds list of facade IDs for security
     * @param permission permission assigned to this card
     */
    public AccessCard(String cardId, List<String> facadeIds, Permission permission) {
        this.cardId = cardId;
        this.facadeIds = Collections.unmodifiableList(new ArrayList<>(facadeIds));
        this.permission = permission;
        this.isActive = true;
        this.creationDate = LocalDateTime.now();
        this.lastUsed = this.creationDate;
    }
    
    /**
     * Returns the card ID.
     * 
     * @return the card ID
     */
    public String getCardId() {
        return cardId;
    }
    
    /**
     * Checks if the card has permission for the specified floor.
     * 
     * @param floor the floor to check permission for
     * @return true if the card has permission, false otherwise
     */
    public boolean hasPermission(Floor floor) {
        return isActive && permission.canAccessFloor(floor);
    }
    
    /**
     * Checks if the card has permission for the specified room.
     * 
     * @param room the room to check permission for
     * @return true if the card has permission, false otherwise
     */
    public boolean hasPermission(String room) {
        return isActive && permission.canAccessRoom(room);
    }
    
    /**
     * Validates access to a floor at the specified time.
     * 
     * @param floor the floor to validate access for
     * @param time the time of the access attempt
     * @return true if access is valid, false otherwise
     */
    public boolean validateAccess(Floor floor, LocalDateTime time) {
        boolean result = isActive && 
                permission.canAccessFloor(floor) && 
                permission.isValidForTime(time);
        
        if (result) {
            lastUsed = time;
        }
        
        return result;
    }
    
    /**
     * Activates or deactivates the card.
     * 
     * @param active true to activate, false to deactivate
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    /**
     * Checks if the card is active.
     * 
     * @return true if the card is active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * Gets the creation date of the card.
     * 
     * @return the creation date
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    
    /**
     * Gets the last used date of the card.
     * 
     * @return the last used date
     */
    public LocalDateTime getLastUsed() {
        return lastUsed;
    }
    
    /**
     * Gets the permission assigned to this card.
     * 
     * @return the permission
     */
    public Permission getPermission() {
        return permission;
    }
    
    /**
     * Encrypts an ID using a time-based algorithm.
     * 
     * @param id the ID to encrypt
     * @param time the time to use for encryption
     * @return the encrypted ID
     */
    private String encryptId(String id, LocalDateTime time) {
        // Simple encryption for demonstration purposes
        return id + "_" + time.toString() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    /**
     * Validates a façade ID.
     * 
     * @param facadeId the façade ID to validate
     * @param time the time to use for validation
     * @return true if the façade ID is valid, false otherwise
     */
    private boolean validateFacadeId(String facadeId, LocalDateTime time) {
        // Simple validation for demonstration purposes
        return facadeIds.contains(facadeId);
    }
}
