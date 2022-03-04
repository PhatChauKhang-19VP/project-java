package app.util;

public class TreatmentLocation extends Location {
    private String id;
    private int capacity, currentRoom;

    public TreatmentLocation() {
        super();
        id = null;
        capacity = currentRoom = 0;
    }

    public TreatmentLocation(String id, int capacity, int currentRoom) {
        this.id = id;
        this.currentRoom = currentRoom;
        this.capacity = capacity;
    }

    public TreatmentLocation(String addressLine, String ward, String district, String province, String id, int capacity, int currentRoom) {
        super(addressLine, ward, district, province);
        this.id = id;
        this.capacity = capacity;
        this.currentRoom = currentRoom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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
        return super.toString() + "\n\t ID: " + id + "\n\tCurrent room: " + currentRoom + "/" + capacity;
    }
}
