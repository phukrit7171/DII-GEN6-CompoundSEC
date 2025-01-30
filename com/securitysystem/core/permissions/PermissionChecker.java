package com.securitysystem.core.permissions;

import com.securitysystem.core.Card;
import com.securitysystem.core.Location;

public interface PermissionChecker {
    boolean hasPermission(Card card, Location location);
}
