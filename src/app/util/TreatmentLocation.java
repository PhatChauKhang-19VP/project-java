package app.util;

public class TreatmentLocation {
    private String name;
    private int capacity, currentRoom;

    public TreatmentLocation() {
        super();
        name = null;
        capacity = currentRoom = 0;
    }

    public TreatmentLocation(String name, int capacity, int currentRoom) {
        this.name = name;
        this.currentRoom = currentRoom;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(int currentRoom) {
        this.currentRoom = currentRoom;
    }

    public  boolean addPatient() {
        if (currentRoom == capacity) {
            return false;
        }
        currentRoom++;
        return true;
    }
    @Override
    public String toString() {
        return "Name: " + name + "\nCapacity: " + currentRoom + "/" + capacity;
    }
}
