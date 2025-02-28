package com.camt.dii.secure.card.factory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.camt.dii.secure.access.Floor;
import com.camt.dii.secure.card.AccessCard;
import com.camt.dii.secure.card.CardIdentifier;
import com.camt.dii.secure.card.Permission;
import com.camt.dii.secure.card.SimplePermission;

/**
 * Tests for the factory pattern implementation.
 * Tests both StandardCardFactory and SecureCardFactory implementations.
 */
public class CardFactoryTest {
    
    @Test
    void testStandardCardFactory() {
        // Create factory
        CardFactory factory = new StandardCardFactory();
        
        // Create test data
        CardIdentifier identifier = new CardIdentifier("STD-123", "STANDARD", LocalDateTime.now());
        Set<Floor> floors = new HashSet<>(Arrays.asList(Floor.LOW));
        Set<String> rooms = new HashSet<>(Arrays.asList("101"));
        Permission permission = new SimplePermission(floors, rooms);
        
        // Test card creation
        AccessCard card = factory.createCard(identifier, permission);
        
        // Verify card properties
        assertNotNull(card, "Card should be created");
        assertEquals(permission, card.getPermission(), "Card should have the provided permission");
        assertTrue(card.isActive(), "Card should be active by default");
        assertTrue(card.getCardId().contains("STD-123"), "Card ID should contain the serial number");
    }
    
    @Test
    void testSecureCardFactory() {
        // Create factory
        CardFactory factory = new SecureCardFactory();
        
        // Create test data
        CardIdentifier identifier = new CardIdentifier("SEC-456", "SECURE", LocalDateTime.now());
        Set<Floor> floors = new HashSet<>(Arrays.asList(Floor.HIGH));
        Set<String> rooms = new HashSet<>(Arrays.asList("301"));
        Permission permission = new SimplePermission(floors, rooms);
        
        // Test card creation
        AccessCard card = factory.createCard(identifier, permission);
        
        // Verify card properties
        assertNotNull(card, "Card should be created");
        assertEquals(permission, card.getPermission(), "Card should have the provided permission");
        assertTrue(card.isActive(), "Card should be active by default");
        
        // Verify secure card ID format - should contain original ID and additional hash component
        String cardId = card.getCardId();
        assertTrue(cardId.contains("SEC-456"), "Card ID should contain the original serial number");
        assertTrue(cardId.contains("-"), "Secure card ID should contain a separator");
        
        // Verify the ID is longer than the standard format (due to added encryption)
        String originalCardId = "SEC-SECURE-" + LocalDateTime.now().toString().substring(0, 10);
        assertTrue(cardId.length() > originalCardId.length(), 
                "Secure card ID should be longer due to encryption");
    }
    
    @Test
    void testBothFactoriesWithSameInput() {
        // Create factories
        CardFactory standardFactory = new StandardCardFactory();
        CardFactory secureFactory = new SecureCardFactory();
        
        // Create identical test data for both
        LocalDateTime now = LocalDateTime.now();
        CardIdentifier identifier = new CardIdentifier("COMP-789", "TEST", now);
        Set<Floor> floors = new HashSet<>(Arrays.asList(Floor.MEDIUM));
        Set<String> rooms = new HashSet<>(Arrays.asList("201"));
        Permission permission = new SimplePermission(floors, rooms);
        
        // Create cards using both factories
        AccessCard standardCard = standardFactory.createCard(identifier, permission);
        AccessCard secureCard = secureFactory.createCard(identifier, permission);
        
        // Verify both cards have the same permission
        assertEquals(permission, standardCard.getPermission(),
                "Standard card should have the provided permission");
        assertEquals(permission, secureCard.getPermission(),
                "Secure card should have the provided permission");
        
        // Verify cards have different IDs due to different factory implementations
        assertTrue(standardCard.getCardId().length() < secureCard.getCardId().length(),
                "Secure card ID should be longer than standard card ID");
    }
}
