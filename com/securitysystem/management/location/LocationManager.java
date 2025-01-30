package com.securitysystem.management.location;

import com.securitysystem.core.Floor;
import com.securitysystem.core.Room;

import java.util.List;

public interface LocationManager {
    void addFloor(Floor floor);
    void addRoomToFloor(Room room, Floor floor);
    Floor getFloorByNumber(int floorNumber);
    Room getRoomByNumber(String roomNumber, Floor floor);
    List<Floor> getAllFloors();
    List<Room> getRoomsOnFloor(Floor floor);
    // ... other location management operations ...
}
