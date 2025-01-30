package com.securitysystem.core;

import java.util.List;

public class Card {

    private String cardId;
    private String encryptionKey; // For time-based encryption (to be detailed later)
    private List<Floor> floorPermissions;
    private List<Room> roomPermissions;
    private boolean isActive;

    // Constructor, getters, setters, methods (isValidForFloor, isValidForRoom, etc.) will be added later
}
