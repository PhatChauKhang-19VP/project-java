package app.user;

import app.util.Location;

public class Admin extends UserDecorator {
    public Admin(IUser user) {
        super(user);
    }

    public boolean createManager() {
        return false;
    }

    public boolean deactivateManager() {
        return false;
    }

    public boolean addLocation(Location newLocation) {
        try {
            // save new loc to db
        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            System.out.println("Successfully added new location.");
            return true;
        }
    }

    public boolean modifyLocation() {
        return false;
    }

    public boolean deleteLocation() {
        return false;
    }
}
