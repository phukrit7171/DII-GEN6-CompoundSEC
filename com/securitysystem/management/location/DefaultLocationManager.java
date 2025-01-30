package com.securitysystem.management.location;

import com.securitysystem.core.Floor;
import com.securitysystem.core.Room;

import java.util.List;

public class DefaultLocationManager implements LocationManager {

    private LocationStorage locationStorage; // Dependency on LocationStorage

    public DefaultLocationManager(LocationStorage locationStorage) {
        this.locationStorage = locationStorage;
    }

    @Override
    public void addFloor(Floor floor) {
        locationStorage.saveFloor(floor);
    }

    @Override
    public void addRoomToFloor(Room room, Floor floor) {
        locationStorage.saveRoom(room); // Assuming LocationStorage can handle both Floors and Rooms
    }

    @Override
    public Floor getFloorByNumber(int floorNumber) {
        return locationStorage.loadFloorByNumber(floorNumber);
    }

    @Override
    public Room getRoomByNumber(String roomNumber, Floor floor) {
        return locationStorage.loadRoomByNumber(roomNumber, floor);
    }

    @Override
    public List<Floor> getAllFloors() {
        return locationStorage.loadAllFloors();
    }

    @Override
    public List<Room> getRoomsOnFloor(Floor floor) {
        return locationStorage.loadRoomsByFloor(floor);
    }
}
