package com.securitysystem.core;

public class Floor implements Location { // Implements Location interface

    private int floorNumber;
    private String floorName;

    // Constructor, getters, setters, methods (addRoom, getRooms, getLocationId, getLocationName) will be added later

    @Override
    public String getLocationId() {
        return String.valueOf(floorNumber); // Example: Floor number as ID
    }

    @Override
    public String getLocationName() {
        return floorName;
    }
}
