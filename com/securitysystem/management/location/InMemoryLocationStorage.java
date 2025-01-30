package com.securitysystem.management.location;

import com.securitysystem.core.Floor;
import com.securitysystem.core.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryLocationStorage implements LocationStorage {

    private Map<Integer, Floor> floorMap = new HashMap<>();
    private List<Room> roomList = new ArrayList<>(); // Simple list for rooms for now

    @Override
    public void saveFloor(Floor floor) {
        floorMap.put(floor.getFloorNumber(), floor);
    }

    @Override
    public void saveRoom(Room room) {
        roomList.add(room);
    }

    @Override
    public Floor loadFloorByNumber(int floorNumber) {
        return floorMap.get(floorNumber);
    }

    @Override
    public Room loadRoomByNumber(String roomNumber, Floor floor) {
        return roomList.stream()
                .filter(room -> room.getRoomNumber().equals(roomNumber) && room.getFloor().equals(floor))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Floor> loadAllFloors() {
        return new ArrayList<>(floorMap.values());
    }

    @Override
    public List<Room> getRoomsOnFloor(Floor floor) {
        return roomList.stream()
                .filter(room -> room.getFloor().equals(floor))
                .toList(); // or .collect(Collectors.toList()) for older Java versions
    }

    @Override
    public void updateFloor(Floor floor) {
        floorMap.put(floor.getFloorNumber(), floor);
    }

    @Override
    public void updateRoom(Room room) {
        // For in-memory, can just replace if room with same ID exists (need to refine room ID logic for update)
        roomList.removeIf(r -> r.getRoomNumber().equals(room.getRoomNumber()) && r.getFloor().equals(room.getFloor())); // Simple removal for update
        roomList.add(room);
    }

    @Override
    public void deleteFloor(int floorNumber) {
        floorMap.remove(floorNumber);
        roomList.removeIf(room -> room.getFloor().getFloorNumber() == floorNumber); // Remove rooms on deleted floor
    }

    @Override
    public void deleteRoom(String roomNumber, Floor floor) {
        roomList.removeIf(room -> room.getRoomNumber().equals(roomNumber) && room.getFloor().equals(floor));
    }
}
