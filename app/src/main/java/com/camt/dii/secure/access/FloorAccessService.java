package com.camt.dii.secure.access;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;

import com.camt.dii.secure.audit.AuditLogger;
import com.camt.dii.secure.audit.AuditLoggerSingleton;
import com.camt.dii.secure.card.AccessCard;

/**
 * Service that uses polymorphism to work with floor access policies.
 * This class demonstrates polymorphism by using different FloorAccessPolicy implementations
 * interchangeably based on the floor's security level.
 */
public class FloorAccessService {
    
    private final Map<Floor, FloorAccessPolicy> policies;
    private final AuditLogger auditLogger;
    
    /**
     * Creates a new FloorAccessService with default policies for each floor level.
     */
    public FloorAccessService() {
        this.policies = new EnumMap<>(Floor.class);
        this.policies.put(Floor.LOW, new LowFloorAccess());
        this.policies.put(Floor.MEDIUM, new MediumFloorAccess());
        this.policies.put(Floor.HIGH, new HighFloorAccess());
        this.auditLogger = AuditLoggerSingleton.getInstance();
    }
    
    /**
     * Creates a new FloorAccessService with custom policies for each floor level.
     * 
     * @param lowPolicy policy for low-security floors
     * @param mediumPolicy policy for medium-security floors
     * @param highPolicy policy for high-security floors
     */
    public FloorAccessService(FloorAccessPolicy lowPolicy, FloorAccessPolicy mediumPolicy, FloorAccessPolicy highPolicy) {
        this.policies = new EnumMap<>(Floor.class);
        this.policies.put(Floor.LOW, lowPolicy);
        this.policies.put(Floor.MEDIUM, mediumPolicy);
        this.policies.put(Floor.HIGH, highPolicy);
        this.auditLogger = AuditLoggerSingleton.getInstance();
    }
    
    /**
     * Sets the access policy for a specific floor level.
     * 
     * @param floor the floor level to set the policy for
     * @param policy the policy to use for the specified floor level
     */
    public void setAccessPolicy(Floor floor, FloorAccessPolicy policy) {
        policies.put(floor, policy);
    }
    
    /**
     * Validates access to a floor at the current time.
     * 
     * @param card the access card attempting to gain access
     * @param floor the floor being accessed
     * @return true if access is granted, false otherwise
     */
    public boolean checkAccess(AccessCard card, Floor floor) {
        return checkAccess(card, floor, LocalDateTime.now());
    }
    
    /**
     * Validates access to a floor at the specified time.
     * Uses the appropriate policy for the floor level.
     * 
     * @param card the access card attempting to gain access
     * @param floor the floor being accessed
     * @param accessTime the time of the access attempt
     * @return true if access is granted, false otherwise
     */
    public boolean checkAccess(AccessCard card, Floor floor, LocalDateTime accessTime) {
        FloorAccessPolicy policy = policies.get(floor);
        boolean result = policy.validateAccess(card, floor, accessTime);
        
        // Log the access attempt
        auditLogger.logAccessAttempt(
                card.getCardId(),
                "Floor: " + floor.name(),
                result,
                accessTime
        );
        
        return result;
    }
}
