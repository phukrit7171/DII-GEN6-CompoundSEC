package com.camt.dii.secure.audit;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a single audit record in the system.
 * Contains details about an audited event such as access attempts,
 * card creation, modification, or revocation.
 */
public class AuditRecord {
    
    private final UUID recordId;
    private final AuditEventType eventType;
    private final String cardId;
    private final String location;
    private final String userId;
    private final boolean outcome;
    private final LocalDateTime timestamp;
    private final Map<String, String> additionalDetails;
    
    /**
     * Creates a new audit record with the specified details.
     * 
     * @param eventType the type of event being recorded
     * @param cardId ID of the card involved in the event
     * @param location location where the event occurred
     * @param userId ID of the user involved in the event (null for system events)
     * @param outcome outcome of the event (true for success, false for failure)
     * @param timestamp when the event occurred
     */
    public AuditRecord(AuditEventType eventType, String cardId, String location, 
                      String userId, boolean outcome, LocalDateTime timestamp) {
        this.recordId = UUID.randomUUID();
        this.eventType = eventType;
        this.cardId = cardId;
        this.location = location;
        this.userId = userId;
        this.outcome = outcome;
        this.timestamp = timestamp;
        this.additionalDetails = new HashMap<>();
    }
    
    /**
     * Returns the unique identifier for this audit record.
     * 
     * @return the record ID
     */
    public UUID getRecordId() {
        return recordId;
    }
    
    /**
     * Returns the type of event recorded.
     * 
     * @return the event type
     */
    public AuditEventType getEventType() {
        return eventType;
    }
    
    /**
     * Returns the ID of the card involved in the event.
     * 
     * @return the card ID
     */
    public String getCardId() {
        return cardId;
    }
    
    /**
     * Returns the location where the event occurred.
     * 
     * @return the location
     */
    public String getLocation() {
        return location;
    }
    
    /**
     * Returns the ID of the user involved in the event.
     * 
     * @return the user ID, or null for system events
     */
    public String getUserId() {
        return userId;
    }
    
    /**
     * Returns the outcome of the event.
     * 
     * @return true for success, false for failure
     */
    public boolean getOutcome() {
        return outcome;
    }
    
    /**
     * Returns when the event occurred.
     * 
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    /**
     * Returns a map of additional details about the event.
     * 
     * @return map of additional details
     */
    public Map<String, String> getAdditionalDetails() {
        return new HashMap<>(additionalDetails);
    }
    
    /**
     * Adds an additional detail to this audit record.
     * 
     * @param key the detail key
     * @param value the detail value
     */
    public void addDetail(String key, String value) {
        additionalDetails.put(key, value);
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - Card: %s, Location: %s, User: %s, Outcome: %s, Time: %s",
                recordId, eventType, cardId, location, userId, outcome, timestamp);
    }
}
