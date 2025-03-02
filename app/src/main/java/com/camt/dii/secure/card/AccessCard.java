package com.camt.dii.secure.card;

import java.time.LocalDateTime;

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
    private final String facadeIds;
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
    public AccessCard(String cardId, String facadeIds, Permission permission) {
        this.cardId = cardId;
        this.facadeIds = facadeIds;
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
     * Returns the facade ID.
     * 
     * @return the facade ID
     */
    public String getFacadeId() {
        return facadeIds;
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
    public String encryptId(String id, LocalDateTime time) {
        // Create a time signature (hours + minutes as hex)
        String timeSignature = String.format("%02x%02x", 
                time.getHour(), 
                time.getMinute());
        
        // Create a unique daily key based on the day of year and year
        String dailyKey = String.format("%03d%04d", 
                time.getDayOfYear(), 
                time.getYear());
        
        // Combine with a random element for uniqueness
        String randomElement = UUID.randomUUID().toString().substring(0, 8);
        
        // Create the encrypted ID
        return id + "_" + timeSignature + "_" + dailyKey + "_" + randomElement;
    }
    
    /**
     * Validates a façade ID against the list of valid façade IDs.
     * Checks if the façade ID exists and is still valid for the given time.
     * 
     * @param facadeId the façade ID to validate
     * @param time the time to use for validation
     * @return true if the façade ID is valid, false otherwise
     */
    public boolean validateFacadeId(String facadeId, LocalDateTime time) {
        // First check if the façade ID exists in our list
        if (!facadeIds.contains(facadeId)) {
            return false;
        }
        
        // For time-sensitive validation, we can check if the ID was created today
        // and if it's being used within a valid time window
        
        // Extract time components from the façade ID if it follows our encryption format
        if (facadeId.contains("_")) {
            String[] parts = facadeId.split("_");
            if (parts.length >= 3) {
                try {
                    // Check if the daily key matches today
                    String dailyKeyPart = parts[2];
                    int storedDay = Integer.parseInt(dailyKeyPart.substring(0, 3));
                    int storedYear = Integer.parseInt(dailyKeyPart.substring(3));
                    
                    // Check if the stored date matches today's date
                    return (storedDay == time.getDayOfYear() && storedYear == time.getYear());
                } catch (Exception e) {
                    // If parsing fails, fall back to basic validation
                    return true;
                }
            }
        }
        
        // If the façade ID doesn't follow our format or we couldn't parse it,
        // just validate that it exists in our list (already checked above)
        return true;
    }
    
    /**
     * Verifies if an external facade ID is valid for this card at the given time.
     * This can be used for external validation systems.
     * 
     * @param providedFacadeId the facade ID provided for validation
     * @param time the time of the validation attempt
     * @return true if the facade ID is valid, false otherwise
     */
    public boolean verifyExternalFacadeId(String providedFacadeId, LocalDateTime time) {
        // Generate a temporary facade ID for this time and check if it matches the provided one
        String tempId = encryptId(cardId, time);
        
        // First try exact matching (for IDs generated using our exact algorithm)
        if (tempId.equals(providedFacadeId)) {
            return true;
        }
        
        // Then check against our stored facade IDs
        return validateFacadeId(providedFacadeId, time);
    }
}
