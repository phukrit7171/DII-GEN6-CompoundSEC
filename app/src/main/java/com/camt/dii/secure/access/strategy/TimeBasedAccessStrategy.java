// com/camt/dii/secure/access/strategy/TimeBasedAccessStrategy.java
package com.camt.dii.secure.access.strategy;

import com.camt.dii.secure.card.Card;
import com.camt.dii.secure.common.Floor;
import com.camt.dii.secure.card.Permission;
import java.time.LocalDateTime;

public class TimeBasedAccessStrategy implements AccessStrategy {
    @Override
    public boolean checkAccess(Card card, Floor floor, String room) {
        Permission permissions = card.getPermissions();
        return permissions.canAccessFloor(floor) && permissions.canAccessRoom(room) && permissions.isValidForTime(LocalDateTime.now());
    }
}