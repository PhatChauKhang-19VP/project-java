package app.user;

import app.App;
import app.util.History;
import app.util.Location;

public class Admin extends UserDecorator {
    public Admin(IUser user) {
        super(user);
    }

    public boolean createManager(Manager manager) {
        try {
            // save to db
        } catch (Exception e) {
            System.out.println("Error creating new manager: " + e.getMessage());
            return false;
        } finally {
            App.getInstance().addUser(manager);
            return true;
        }
    }

    public void deactivateManager(Manager manager) {
        manager.deactivate();
    }

    public History viewHistory(IUser user) {
        return user.showHistory();
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
