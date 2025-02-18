// com/camt/dii/secure/card/Card.java
package com.camt.dii.secure.card;

public interface Card {
    String getCardId();
    String getFacadeId(); // Facade ID
    boolean isValid();
    Permission getPermissions();
    void setPermissions(Permission permissions);
    boolean isRevoked();
    void revoke();
}