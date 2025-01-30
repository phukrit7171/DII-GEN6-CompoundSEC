package com.securitysystem.management.location;

import com.securitysystem.core.Floor;
import com.securitysystem.core.Room;

import java.util.List;

public interface LocationStorage {
    void saveFloor(Floor floor);
    void saveRoom(Room room);
    Floor loadFloorByNumber(int floorNumber);
    Room loadRoomByNumber(String roomNumber, Floor floor);
    List<Floor> loadAllFloors();
    List<Room> loadRoomsByFloor(Floor floor);
    void updateFloor(Floor floor);
    void updateRoom(Room room);
    void deleteFloor(int floorNumber);
    void deleteRoom(String roomNumber, Floor floor);
    // ... other storage operations ...
}
