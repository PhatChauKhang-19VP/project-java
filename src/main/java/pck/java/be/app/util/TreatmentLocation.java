package pck.java.be.app.util;

public class TreatmentLocation {
    private String code, name;
    private int capacity, currentRoom;

    public TreatmentLocation(String code, String name, int capacity, int currentRoom) {
        this.code = code;
        this.name = name;
        this.capacity = capacity;
        this.currentRoom = currentRoom;
    }

    public TreatmentLocation() {
        super();
        name = null;
        capacity = currentRoom = 0;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
