package com.camt.dii.secure.card;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Tests for the CardIdentifier class.
 * Focuses on testing the proper implementation of Object class method overrides.
 */
public class CardIdentifierTest {
    
    @Test
    void testEquals() {
        // Create identifiers with same serial and issuer but different dates
        LocalDateTime now = LocalDateTime.now();
        CardIdentifier id1 = new CardIdentifier("SERIAL123", "ISSUER1", now);
        CardIdentifier id2 = new CardIdentifier("SERIAL123", "ISSUER1", now.plusDays(1));
        CardIdentifier id3 = new CardIdentifier("SERIAL123", "ISSUER2", now);
        CardIdentifier id4 = new CardIdentifier("SERIAL456", "ISSUER1", now);
        
        // Test equality - should be equal if serial and issuer match, regardless of date
        assertTrue(id1.equals(id2), "Cards with same serial and issuer should be equal even with different dates");
        
        // Test inequality
        assertFalse(id1.equals(id3), "Cards with different issuers should not be equal");
        assertFalse(id1.equals(id4), "Cards with different serials should not be equal");
        assertFalse(id1.equals(null), "Card should not be equal to null");
        assertFalse(id1.equals("NotACard"), "Card should not be equal to other object types");
    }
    
    @Test
    void testHashCode() {
        // Create identifiers with same serial and issuer but different dates
        LocalDateTime now = LocalDateTime.now();
        CardIdentifier id1 = new CardIdentifier("SERIAL123", "ISSUER1", now);
        CardIdentifier id2 = new CardIdentifier("SERIAL123", "ISSUER1", now.plusDays(1));
        CardIdentifier id3 = new CardIdentifier("SERIAL123", "ISSUER2", now);
        
        // Test hash code consistency with equals
        assertEquals(id1.hashCode(), id2.hashCode(), 
                "Equal objects should have equal hash codes");
        assertNotEquals(id1.hashCode(), id3.hashCode(), 
                "Unequal objects should have different hash codes");
    }
    
    @Test
    void testToString() {
        // Create an identifier
        LocalDateTime date = LocalDateTime.of(2023, 1, 1, 0, 0);
        CardIdentifier id = new CardIdentifier("SERIAL123", "ISSUER1", date);
        
        // Test that toString contains all important fields
        String str = id.toString();
        assertTrue(str.contains("SERIAL123"), "ToString should contain the serial number");
        assertTrue(str.contains("ISSUER1"), "ToString should contain the issuer ID");
        assertTrue(str.contains("2023-01-01"), "ToString should contain the formatted date");
    }
    
    @Test
    void testCompareTo() {
        // Create identifiers to test ordering
        CardIdentifier id1 = new CardIdentifier("SERIAL123", "ISSUER1", LocalDateTime.now());
        CardIdentifier id2 = new CardIdentifier("SERIAL123", "ISSUER2", LocalDateTime.now());
        CardIdentifier id3 = new CardIdentifier("SERIAL456", "ISSUER1", LocalDateTime.now());
        
        // Test ordering - first by issuer, then by serial
        assertTrue(id1.compareTo(id2) < 0, "ISSUER1 should come before ISSUER2");
        assertTrue(id2.compareTo(id1) > 0, "ISSUER2 should come after ISSUER1");
        assertTrue(id1.compareTo(id3) < 0, "SERIAL123 should come before SERIAL456 with same issuer");
        assertTrue(id3.compareTo(id1) > 0, "SERIAL456 should come after SERIAL123 with same issuer");
        assertEquals(0, id1.compareTo(new CardIdentifier("SERIAL123", "ISSUER1", LocalDateTime.now())), 
                "Identical cards should have compareTo = 0");
    }
    
    @Test
    void testGetters() {
        // Create an identifier
        LocalDateTime date = LocalDateTime.of(2023, 1, 1, 0, 0);
        CardIdentifier id = new CardIdentifier("SERIAL123", "ISSUER1", date);
        
        // Test getters
        assertEquals("SERIAL123", id.getSerialNumber(), "getSerialNumber should return the serial number");
        assertEquals("ISSUER1", id.getIssuerId(), "getIssuerId should return the issuer ID");
        assertEquals(date, id.getIssueDate(), "getIssueDate should return the issue date");
    }
}
