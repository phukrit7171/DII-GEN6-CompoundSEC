package com.camt.dii.secure.card.Permission;

import com.camt.dii.secure.common.Floor;

public interface Permission {
    boolean canAccessFloor(Floor floor);
    boolean canAccessRoom(String room);
}