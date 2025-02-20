package com.camt.dii.secure.card.Permission;

import com.camt.dii.secure.common.Floor;
import java.util.Set;

public class SimplePermission implements Permission {
    private Set<Floor> allowedFloors;
    private Set<String> allowedRooms;

    public SimplePermission(Set<Floor> allowedFloors, Set<String> allowedRooms) {
        this.allowedFloors = allowedFloors;
        this.allowedRooms = allowedRooms;
    }

    @Override
    public boolean canAccessFloor(Floor floor) {
        return allowedFloors.contains(floor);
    }

    @Override
    public boolean canAccessRoom(String room) {
        return allowedRooms.contains(room);
    }
}