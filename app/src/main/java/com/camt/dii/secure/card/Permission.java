// com/camt/dii/secure/card/Permission.java
package com.camt.dii.secure.card;

import com.camt.dii.secure.common.Floor;
import java.time.LocalDateTime;

public interface Permission {
    boolean canAccessFloor(Floor floor);
    boolean canAccessRoom(String room);
    boolean isValidForTime(LocalDateTime time);
}