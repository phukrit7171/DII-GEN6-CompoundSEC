package com.securitysystem.core;

/**
 * Interface representing a location in the security system (Floor or Room).
 */
public interface Location {
    String getLocationId(); // Unique identifier for the location
    String getLocationName(); // Human-readable name of the location
}
