package com.camt.dii.secure;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.camt.dii.secure.access.Floor;
import com.camt.dii.secure.access.FloorAccessPolicy;
import com.camt.dii.secure.access.FloorAccessService;
import com.camt.dii.secure.access.HighFloorAccess;
import com.camt.dii.secure.access.LowFloorAccess;
import com.camt.dii.secure.access.MediumFloorAccess;
import com.camt.dii.secure.audit.AuditLogger;
import com.camt.dii.secure.audit.AuditLoggerSingleton;
import com.camt.dii.secure.audit.decorator.DetailedAuditLogDecorator;
import com.camt.dii.secure.card.AccessCard;
import com.camt.dii.secure.card.CardIdentifier;
import com.camt.dii.secure.card.Permission;
import com.camt.dii.secure.card.SimplePermission;
import com.camt.dii.secure.card.TimeLimitedPermission;
import com.camt.dii.secure.card.factory.CardFactory;
import com.camt.dii.secure.card.factory.SecureCardFactory;
import com.camt.dii.secure.card.factory.StandardCardFactory;

/**
 * Main application that demonstrates the access control system.
 */
public class App {
    
    public static void main(String[] args) {
        System.out.println("===== Access Control System Demo =====");
        
        // Get the singleton audit logger instance
        AuditLogger auditLogger = AuditLoggerSingleton.getInstance();
        
        // Decorate the audit logger with detailed logging
        AuditLogger detailedLogger = new DetailedAuditLogDecorator(auditLogger);
        
        // Create card factories
        CardFactory standardFactory = new StandardCardFactory();
        CardFactory secureFactory = new SecureCardFactory();
        
        // Create permissions
        Set<Floor> allFloors = new HashSet<>(Arrays.asList(Floor.LOW, Floor.MEDIUM, Floor.HIGH));
        Set<Floor> basicFloors = new HashSet<>(Arrays.asList(Floor.LOW));
        Set<String> allRooms = new HashSet<>(Arrays.asList("101", "102", "201", "301"));
        Set<String> basicRooms = new HashSet<>(Arrays.asList("101"));
        
        Permission adminPermission = new SimplePermission(allFloors, allRooms);
        Permission basicPermission = new SimplePermission(basicFloors, basicRooms);
        Permission visitorPermission = new TimeLimitedPermission(
                basicFloors, 
                basicRooms,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );
        
        // Create card identifiers
        CardIdentifier adminId = new CardIdentifier("ADM-001", "ADMIN", LocalDateTime.now());
        CardIdentifier employeeId = new CardIdentifier("EMP-001", "EMPLOYEE", LocalDateTime.now());
        CardIdentifier visitorId = new CardIdentifier("VIS-001", "VISITOR", LocalDateTime.now());
        
        // Create cards using factories
        AccessCard adminCard = secureFactory.createCard(adminId, adminPermission);
        AccessCard employeeCard = standardFactory.createCard(employeeId, basicPermission);
        AccessCard visitorCard = standardFactory.createCard(visitorId, visitorPermission);
        
        System.out.println("Created cards:");
        System.out.println("Admin Card: " + adminCard.getCardId() + 
                           " (Created: " + adminCard.getCreationDate() + ")");
        System.out.println("Employee Card: " + employeeCard.getCardId() + 
                          " (Created: " + employeeCard.getCreationDate() + ")");
        System.out.println("Visitor Card: " + visitorCard.getCardId() + 
                          " (Created: " + visitorCard.getCreationDate() + ")");
        
        // Create floor access policies
        FloorAccessPolicy lowPolicy = new LowFloorAccess();
        FloorAccessPolicy mediumPolicy = new MediumFloorAccess(
                LocalTime.of(9, 0),  // 9:00 AM
                LocalTime.of(17, 0)  // 5:00 PM
        );
        FloorAccessPolicy highPolicy = new HighFloorAccess();
        
        // Create floor access service
        FloorAccessService floorService = new FloorAccessService(lowPolicy, mediumPolicy, highPolicy);
        
        // Simulate access attempts
        System.out.println("\nSimulating access attempts:");
        
        LocalDateTime now = LocalDateTime.now();
        
        // Admin card access
        simulateAccess(floorService, adminCard, Floor.LOW, now, "Admin to Low Floor");
        simulateAccess(floorService, adminCard, Floor.MEDIUM, now, "Admin to Medium Floor");
        simulateAccess(floorService, adminCard, Floor.HIGH, now, "Admin to High Floor");
        
        // Employee card access
        simulateAccess(floorService, employeeCard, Floor.LOW, now, "Employee to Low Floor");
        simulateAccess(floorService, employeeCard, Floor.MEDIUM, now, "Employee to Medium Floor");
        simulateAccess(floorService, employeeCard, Floor.HIGH, now, "Employee to High Floor");
        
        // Visitor card access
        simulateAccess(floorService, visitorCard, Floor.LOW, now, "Visitor to Low Floor");
        simulateAccess(floorService, visitorCard, Floor.MEDIUM, now, "Visitor to Medium Floor");
        simulateAccess(floorService, visitorCard, Floor.HIGH, now, "Visitor to High Floor");
        
        // Check card permissions directly
        System.out.println("\nChecking card permissions directly:");
        System.out.println(" - Admin card permission type: " + adminCard.getPermission().getClass().getSimpleName());
        System.out.println(" - Admin card can access LOW floor: " + adminCard.hasPermission(Floor.LOW));
        System.out.println(" - Admin card can access room 101: " + adminCard.hasPermission("101"));
        System.out.println(" - Admin card can access room 999: " + adminCard.hasPermission("999"));
                
        // Simulate multiple accesses to update last used time
        System.out.println("\nSimulating multiple accesses to track usage times:");
        LocalDateTime firstAccessTime = now;
        System.out.println(" - Admin card initial access time: " + adminCard.getLastUsed());
        boolean isInitialTimeEqualToFirstAccess = adminCard.getLastUsed().equals(firstAccessTime);
        System.out.println(" - Is initial time equal to first access time: " + isInitialTimeEqualToFirstAccess);
        
        // Sleep for a moment to ensure time difference
        try { Thread.sleep(1000); } catch (InterruptedException e) { /* ignore */ }
        
        LocalDateTime secondAccessTime = LocalDateTime.now();
        adminCard.validateAccess(Floor.LOW, secondAccessTime);
        System.out.println(" - Admin card after access: " + adminCard.getLastUsed());
        System.out.println(" - Time difference demonstrated: " + 
                          !adminCard.getLastUsed().equals(firstAccessTime) + 
                          " (first: " + firstAccessTime + ", current: " + adminCard.getLastUsed() + ")");
        
        // Simulate card revocation
        System.out.println("\nSimulating card revocation:");
        visitorCard.setActive(false);
        detailedLogger.logCardRevocation(visitorCard.getCardId(), "ADMIN", LocalDateTime.now());
        simulateAccess(floorService, visitorCard, Floor.LOW, now, "Revoked Visitor to Low Floor");
        System.out.println(" - Visitor card active status: " + visitorCard.isActive());
        
        System.out.println("\nAccess Control System Demo completed successfully!");
    }
    
    /**
     * Simulates an access attempt and prints the result.
     * 
     * @param service the floor access service
     * @param card the access card
     * @param floor the floor being accessed
     * @param time the time of the access attempt
     * @param description description of the access attempt
     */
    private static void simulateAccess(FloorAccessService service, AccessCard card, 
                                     Floor floor, LocalDateTime time, String description) {
        boolean result = service.checkAccess(card, floor, time);
        System.out.println(" - " + description + ": " + (result ? "GRANTED" : "DENIED"));
    }
}
