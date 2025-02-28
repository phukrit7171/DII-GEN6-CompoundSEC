/*
 * Tests for the Access Control System
 */
package com.camt.dii.secure;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.camt.dii.secure.access.Floor;
import com.camt.dii.secure.card.AccessCard;
import com.camt.dii.secure.card.CardIdentifier;
import com.camt.dii.secure.card.Permission;
import com.camt.dii.secure.card.SimplePermission;
import com.camt.dii.secure.card.TimeLimitedPermission;
import com.camt.dii.secure.card.factory.CardFactory;
import com.camt.dii.secure.card.factory.StandardCardFactory;

/**
 * Integration tests for the Access Control System
 */
class AppTest {
    
    private CardFactory cardFactory;
    private Permission adminPermission;
    private Permission visitorPermission;
    private CardIdentifier adminId;
    private CardIdentifier visitorId;
    private AccessCard adminCard;
    private AccessCard visitorCard;
    private LocalDateTime now;
    
    @BeforeEach
    void setUp() {
        // Create common test objects
        cardFactory = new StandardCardFactory();
        
        // Create permissions
        Set<Floor> allFloors = new HashSet<>(Arrays.asList(Floor.LOW, Floor.MEDIUM, Floor.HIGH));
        Set<Floor> basicFloors = new HashSet<>(Arrays.asList(Floor.LOW));
        Set<String> allRooms = new HashSet<>(Arrays.asList("101", "102", "201", "301"));
        Set<String> basicRooms = new HashSet<>(Arrays.asList("101"));
        
        adminPermission = new SimplePermission(allFloors, allRooms);
        now = LocalDateTime.now();
        visitorPermission = new TimeLimitedPermission(
                basicFloors, 
                basicRooms,
                now,
                now.plusDays(1)
        );
        
        // Create card identifiers
        adminId = new CardIdentifier("ADM-001", "ADMIN", now);
        visitorId = new CardIdentifier("VIS-001", "VISITOR", now);
        
        // Create cards using factories
        adminCard = cardFactory.createCard(adminId, adminPermission);
        visitorCard = cardFactory.createCard(visitorId, visitorPermission);
    }
    
    @Test 
    void testCardCreation() {
        // Verify cards are created with correct properties
        assertNotNull(adminCard, "Admin card should be created");
        assertNotNull(visitorCard, "Visitor card should be created");
        
        // Verify card IDs are correctly formed
        assertTrue(adminCard.getCardId().contains("ADM-001"), "Admin card ID should contain the serial number");
        assertTrue(visitorCard.getCardId().contains("VIS-001"), "Visitor card ID should contain the serial number");
        
        // Verify cards have the right permissions
        assertEquals(adminPermission, adminCard.getPermission(), "Admin card should have admin permissions");
        assertEquals(visitorPermission, visitorCard.getPermission(), "Visitor card should have visitor permissions");
        
        // Verify cards are active by default
        assertTrue(adminCard.isActive(), "Admin card should be active by default");
        assertTrue(visitorCard.isActive(), "Visitor card should be active by default");
    }
    
    @Test
    void testCardPermissions() {
        // Test admin card permissions
        assertTrue(adminCard.hasPermission(Floor.LOW), "Admin should have access to LOW floor");
        assertTrue(adminCard.hasPermission(Floor.MEDIUM), "Admin should have access to MEDIUM floor");
        assertTrue(adminCard.hasPermission(Floor.HIGH), "Admin should have access to HIGH floor");
        assertTrue(adminCard.hasPermission("101"), "Admin should have access to room 101");
        assertTrue(adminCard.hasPermission("301"), "Admin should have access to room 301");
        
        // Test visitor card permissions
        assertTrue(visitorCard.hasPermission(Floor.LOW), "Visitor should have access to LOW floor");
        assertFalse(visitorCard.hasPermission(Floor.MEDIUM), "Visitor should not have access to MEDIUM floor");
        assertFalse(visitorCard.hasPermission(Floor.HIGH), "Visitor should not have access to HIGH floor");
        assertTrue(visitorCard.hasPermission("101"), "Visitor should have access to room 101");
        assertFalse(visitorCard.hasPermission("301"), "Visitor should not have access to room 301");
    }
    
    @Test
    void testCardDeactivation() {
        // Initial state
        assertTrue(adminCard.isActive(), "Admin card should start active");
        
        // Deactivate the card
        adminCard.setActive(false);
        assertFalse(adminCard.isActive(), "Admin card should be inactive after deactivation");
        
        // Reactivate the card
        adminCard.setActive(true);
        assertTrue(adminCard.isActive(), "Admin card should be active after reactivation");
    }
    
    @Test
    void testTimeLimitedPermission() {
        // Test valid time access
        LocalDateTime validTime = now.plusHours(1);
        assertTrue(visitorCard.validateAccess(Floor.LOW, validTime), 
                "Visitor should have access to LOW floor during valid time");
        
        // Test expired time access
        LocalDateTime expiredTime = now.plusDays(2);
        assertFalse(visitorCard.validateAccess(Floor.LOW, expiredTime), 
                "Visitor should not have access to LOW floor after permission expires");
    }
    
    @Test
    void testLastUsedTimeUpdates() {
        // Get initial last used time
        LocalDateTime initialTime = visitorCard.getLastUsed();
        
        // Wait a moment
        try { Thread.sleep(10); } catch (InterruptedException e) { /* ignore */ }
        
        // Access with a new time
        LocalDateTime accessTime = LocalDateTime.now();
        visitorCard.validateAccess(Floor.LOW, accessTime);
        
        // Check that last used time was updated
        assertNotEquals(initialTime, visitorCard.getLastUsed(), 
                "Last used time should be updated after access");
    }
}
