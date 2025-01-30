package com.securitysystem.core;

public class Room implements Location { // Implements Location interface

    private String roomNumber;
    private String roomName;
    private Floor floor; // Reference to the Floor it belongs to

    // Constructor, getters, setters, methods (getLocationId, getLocationName) will be added later

    @Override
    public String getLocationId() {
        return roomNumber; // Example: Room number as ID
    }

    @Override
    public String getLocationName() {
        return roomName;
    }
}
