// com/camt/dii/secure/card/SimplePermission.java
package com.camt.dii.secure.card;

import com.camt.dii.secure.common.Floor;
import java.time.LocalDateTime;

public class SimplePermission implements Permission {
    private final boolean allowLowFloor;
    private final boolean allowMediumFloor;
    private final boolean allowHighFloor;
    private final String allowedRoom;

    public SimplePermission(boolean allowLowFloor, boolean allowMediumFloor, boolean allowHighFloor, String allowedRoom) {
        this.allowLowFloor = allowLowFloor;
        this.allowMediumFloor = allowMediumFloor;
        this.allowHighFloor = allowHighFloor;
        this.allowedRoom = allowedRoom;
    }

    public SimplePermission() {
        this(false, false, false, null);
    }


    @Override
    public boolean canAccessFloor(Floor floor) {
        switch (floor) {
            case LOW: return allowLowFloor;
            case MEDIUM: return allowMediumFloor;
            case HIGH: return allowHighFloor;
            default: return false;
        }
    }

    @Override
    public boolean canAccessRoom(String room) {
        return allowedRoom != null && allowedRoom.equals(room);
    }

    @Override
    public boolean isValidForTime(LocalDateTime time) {
        return true; // Simple permission is always valid for time in this example
    }
}