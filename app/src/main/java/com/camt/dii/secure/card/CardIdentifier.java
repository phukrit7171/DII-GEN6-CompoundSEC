package com.camt.dii.secure.card;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents a unique card identifier.
 * Demonstrates proper use of Object class method overrides for equality testing
 * and string representation.
 */
public class CardIdentifier implements Comparable<CardIdentifier> {
    
    private final String serialNumber;
    private final String issuerId;
    private final LocalDateTime issueDate;
    
    /**
     * Creates a new CardIdentifier with the specified details.
     * 
     * @param serialNumber physical card serial number
     * @param issuerId ID of the issuing authority
     * @param issueDate when the card was issued
     */
    public CardIdentifier(String serialNumber, String issuerId, LocalDateTime issueDate) {
        this.serialNumber = serialNumber;
        this.issuerId = issuerId;
        this.issueDate = issueDate;
    }
    
    /**
     * Gets the card's serial number.
     * 
     * @return the serial number
     */
    public String getSerialNumber() {
        return serialNumber;
    }
    
    /**
     * Gets the ID of the issuing authority.
     * 
     * @return the issuer ID
     */
    public String getIssuerId() {
        return issuerId;
    }
    
    /**
     * Gets the issue date of the card.
     * 
     * @return the issue date
     */
    public LocalDateTime getIssueDate() {
        return issueDate;
    }
    
    /**
     * Compares this CardIdentifier with another one.
     * Cards are ordered first by issuer ID, then by serial number.
     * 
     * @param other the CardIdentifier to compare with
     * @return negative if this card is less than the other, zero if equal, positive if greater
     */
    @Override
    public int compareTo(CardIdentifier other) {
        int issuerComparison = this.issuerId.compareTo(other.issuerId);
        if (issuerComparison != 0) {
            return issuerComparison;
        }
        return this.serialNumber.compareTo(other.serialNumber);
    }
    
    /**
     * Checks if this CardIdentifier is equal to another object.
     * Two CardIdentifiers are considered equal if they have the same serial number and issuer ID,
     * regardless of the issue date.
     * 
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        CardIdentifier other = (CardIdentifier) obj;
        return Objects.equals(serialNumber, other.serialNumber) &&
               Objects.equals(issuerId, other.issuerId);
    }
    
    /**
     * Returns a hash code for this CardIdentifier.
     * The hash code is based on the serial number and issuer ID,
     * consistent with the equals method.
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(serialNumber, issuerId);
    }
    
    /**
     * Returns a string representation of this CardIdentifier.
     * 
     * @return a string representation
     */
    @Override
    public String toString() {
        return "Card[" + serialNumber + ", Issuer:" + issuerId + 
               ", Issued:" + issueDate.format(DateTimeFormatter.ISO_DATE) + "]";
    }
}
