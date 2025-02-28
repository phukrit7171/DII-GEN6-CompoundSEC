package com.camt.dii.secure.access;

/**
 * Represents different floor security levels in the building.
 * Used for access control decisions.
 */
public enum Floor {
    LOW(1),
    MEDIUM(2),
    HIGH(3);
    
    private final int securityLevel;
    
    Floor(int securityLevel) {
        this.securityLevel = securityLevel;
    }
    
    /**
     * Returns the numerical security level of this floor.
     * Higher numbers indicate higher security requirements.
     * 
     * @return the security level as an integer
     */
    public int getSecurityLevel() {
        return securityLevel;
    }
}
