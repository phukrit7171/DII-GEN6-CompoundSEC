package com.securitysystem.management.location;

import com.securitysystem.core.Floor;
import com.securitysystem.core.Room;

import java.util.List;

public class FileLocationStorage implements LocationStorage {

    @Override
    public void saveFloor(Floor floor) {
        // Implementation to save floor to a file will be added
    }

    @Override
    public void saveRoom(Room room) {
        // Implementation to save room to a file will be added
    }

    @Override
    public Floor loadFloorByNumber(int floorNumber) {
        // Implementation to load floor from a file will be added
        return null; // Placeholder
    }

    @Override
    public Room loadRoomByNumber(String roomNumber, Floor floor) {
        // Implementation to load room from a file will be added
        return null; // Placeholder
    }

    @Override
    public List<Floor> loadAllFloors() {
        // Implementation to load all floors from a file will be added
        return null; // Placeholder
    }

    @Override
    public List<Room> loadRoomsByFloor(Floor floor) {
        // Implementation to load rooms on a floor from a file will be added
        return null; // Placeholder
    }

    @Override
    public void updateFloor(Floor floor) {
        // Implementation to update floor in a file will be added
    }

    @Override
    public void updateRoom(Room room) {
        // Implementation to update room in a file will be added
    }

    @Override
    public void deleteFloor(int floorNumber) {
        // Implementation to delete floor from a file will be added
    }

    @Override
    public void deleteRoom(String roomNumber, Floor floor) {
        // Implementation to delete room from a file will be added
    }
}
