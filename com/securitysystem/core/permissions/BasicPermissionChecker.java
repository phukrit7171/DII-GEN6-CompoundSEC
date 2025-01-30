package com.securitysystem.core.permissions;

import com.securitysystem.core.Card;
import com.securitysystem.core.Location;

public class BasicPermissionChecker implements PermissionChecker {

    @Override
    public boolean hasPermission(Card card, Location location) {
        // Basic permission checking logic will be implemented here
        return false; // Placeholder
    }
}
