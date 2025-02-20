package com.camt.dii.secure.strategy;

import com.camt.dii.secure.card.Card;
import com.camt.dii.secure.common.Floor;

public class RuleBasedAccessStrategy implements AccessStrategy {
    @Override
    public boolean checkAccess(Card card, Floor floor, String room) {
        return card.getPermissions().canAccessFloor(floor) 
            && card.getPermissions().canAccessRoom(room);
    }
}