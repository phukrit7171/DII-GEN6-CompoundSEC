// com/camt/dii/secure/access/strategy/RuleBasedAccessStrategy.java
package com.camt.dii.secure.access.strategy;

import com.camt.dii.secure.card.Card;
import com.camt.dii.secure.common.Floor;
import com.camt.dii.secure.card.Permission;

public class RuleBasedAccessStrategy implements AccessStrategy {
    @Override
    public boolean checkAccess(Card card, Floor floor, String room) {
        Permission permissions = card.getPermissions();
        return permissions.canAccessFloor(floor) && permissions.canAccessRoom(room);
    }
}