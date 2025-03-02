package com.camt.dii.secure.access;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.camt.dii.secure.card.AccessCard;
import com.camt.dii.secure.card.CardIdentifier;
import com.camt.dii.secure.card.Permission;
import com.camt.dii.secure.card.SimplePermission;
import com.camt.dii.secure.card.factory.CardFactory;
import com.camt.dii.secure.card.factory.StandardCardFactory;

/**
 * Tests for the floor access policy implementations.
 * Focuses on testing polymorphism with the different policy classes.
 */
public class FloorAccessPolicyTest {
    
    private CardFactory cardFactory;
    private AccessCard adminCard;
    private AccessCard employeeCard;
    private AccessCard visitorCard;
    private LocalDateTime now;
    
    @BeforeEach
    void setUp() {
        cardFactory = new StandardCardFactory();
        now = LocalDateTime.now();
        
        // Create permissions
        Set<Floor> allFloors = new HashSet<>(Arrays.asList(Floor.LOW, Floor.MEDIUM, Floor.HIGH));
        Set<Floor> mediumFloors = new HashSet<>(Arrays.asList(Floor.LOW, Floor.MEDIUM));
        Set<Floor> basicFloors = new HashSet<>(Arrays.asList(Floor.LOW));
        Set<String> allRooms = new HashSet<>(Arrays.asList("101", "102", "201", "301"));
        
        Permission adminPermission = new SimplePermission(allFloors, allRooms);
        Permission employeePermission = new SimplePermission(mediumFloors, allRooms);
        Permission visitorPermission = new SimplePermission(basicFloors, allRooms);
        
        // Create cards
        CardIdentifier adminId = new CardIdentifier("ADMIN-001", "ADMIN", now);
        CardIdentifier employeeId = new CardIdentifier("EMP-001", "EMPLOYEE", now);
        CardIdentifier visitorId = new CardIdentifier("VIS-001", "VISITOR", now);
        
        adminCard = cardFactory.createCard(adminId, adminPermission);
        employeeCard = cardFactory.createCard(employeeId, employeePermission);
        visitorCard = cardFactory.createCard(visitorId, visitorPermission);
    }
    
    @Test
    void testLowFloorAccess() {
        // Create policy
        FloorAccessPolicy policy = new LowFloorAccess();
        
        // Test with all card types
        assertTrue(policy.validateAccess(adminCard, Floor.LOW, now),
                "Admin should have access to LOW floor with LowFloorAccess policy");
        assertTrue(policy.validateAccess(employeeCard, Floor.LOW, now),
                "Employee should have access to LOW floor with LowFloorAccess policy");
        assertTrue(policy.validateAccess(visitorCard, Floor.LOW, now),
                "Visitor should have access to LOW floor with LowFloorAccess policy");
        
        // Test with inactive card
        adminCard.setActive(false);
        assertFalse(policy.validateAccess(adminCard, Floor.LOW, now),
                "Inactive admin card should not have access to LOW floor");
        adminCard.setActive(true);
        
        // Test with wrong floor
        assertFalse(policy.validateAccess(visitorCard, Floor.MEDIUM, now),
                "Visitor should not have access to MEDIUM floor");
    }
    
    @Test
    void testMediumFloorAccessWithTimeRestriction() {
        // Create policy with business hours (9 AM - 5 PM)
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(17, 0);
        FloorAccessPolicy policy = new MediumFloorAccess(startTime, endTime);
        
        // Create times for testing
        LocalDateTime duringBusinessHours = LocalDateTime.of(
                now.toLocalDate(), 
                LocalTime.of(12, 0)); // Noon
        
        LocalDateTime outsideBusinessHours = LocalDateTime.of(
                now.toLocalDate(), 
                LocalTime.of(20, 0)); // 8 PM
        
        // Test during business hours
        assertTrue(policy.validateAccess(adminCard, Floor.MEDIUM, duringBusinessHours),
                "Admin should have access to MEDIUM floor during business hours");
        assertTrue(policy.validateAccess(employeeCard, Floor.MEDIUM, duringBusinessHours),
                "Employee should have access to MEDIUM floor during business hours");
        assertFalse(policy.validateAccess(visitorCard, Floor.MEDIUM, duringBusinessHours),
                "Visitor should not have access to MEDIUM floor even during business hours");
        
        // Test outside business hours
        assertFalse(policy.validateAccess(adminCard, Floor.MEDIUM, outsideBusinessHours),
                "Admin should not have access to MEDIUM floor outside business hours");
        assertFalse(policy.validateAccess(employeeCard, Floor.MEDIUM, outsideBusinessHours),
                "Employee should not have access to MEDIUM floor outside business hours");
    }
    
    @Test
    void testHighFloorAccess() {
        // Create high floor policy with default settings
        FloorAccessPolicy policy = new HighFloorAccess();
        
        // Set time to noon on a Monday for testing within working hours
        now = LocalDateTime.of(2025, 3, 3, 12, 0);

        // Test with all card types on correct floor
        assertTrue(policy.validateAccess(adminCard, Floor.HIGH, now),
                "Admin should have access to HIGH floor with HighFloorAccess policy");
        assertFalse(policy.validateAccess(employeeCard, Floor.HIGH, now),
                "Employee should not have access to HIGH floor");
        assertFalse(policy.validateAccess(visitorCard, Floor.HIGH, now),
                "Visitor should not have access to HIGH floor");

        // Test with wrong floor level - high security policy should only work on HIGH floors
        assertFalse(policy.validateAccess(adminCard, Floor.MEDIUM, now),
                "High security policy should only allow access to HIGH floors");
        assertFalse(policy.validateAccess(adminCard, Floor.LOW, now),
                "High security policy should only allow access to HIGH floors");
    }

    @Test
    void testFloorAccessService() {
        // Create policies
        FloorAccessPolicy lowPolicy = new LowFloorAccess();
        FloorAccessPolicy mediumPolicy = new MediumFloorAccess();
        FloorAccessPolicy highPolicy = new HighFloorAccess();
        
        // Create service with policies
        FloorAccessService service = new FloorAccessService(lowPolicy, mediumPolicy, highPolicy);
        
        // Test service behavior
        assertTrue(service.checkAccess(adminCard, Floor.LOW, now),
                "Admin should have access to LOW floor via service");
        assertTrue(service.checkAccess(employeeCard, Floor.LOW, now),
                "Employee should have access to LOW floor via service");
        assertTrue(service.checkAccess(visitorCard, Floor.LOW, now),
                "Visitor should have access to LOW floor via service");
        
        // Test changing policies
        // Create a strict low floor policy that denies all access
        FloorAccessPolicy strictLowPolicy = new FloorAccessPolicy() {
            @Override
            public boolean validateAccess(AccessCard card, Floor floor, LocalDateTime accessTime) {
                return false; // Always deny
            }
        };
        
        // Set the new policy
        service.setAccessPolicy(Floor.LOW, strictLowPolicy);
        
        // Test with new policy
        assertFalse(service.checkAccess(adminCard, Floor.LOW, now),
                "Admin should not have access to LOW floor with strict policy");
        assertFalse(service.checkAccess(employeeCard, Floor.LOW, now),
                "Employee should not have access to LOW floor with strict policy");
        assertFalse(service.checkAccess(visitorCard, Floor.LOW, now),
                "Visitor should not have access to LOW floor with strict policy");
    }
}
