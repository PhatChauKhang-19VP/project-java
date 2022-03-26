package app.user;

import app.App;
import app.util.History;
import app.util.TreatmentLocation;

/** Admin decorator, also implemented with Singleton pattern. */
public class Admin extends UserDecorator {

    private static Admin instance = null;
    private Admin() {}

    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
            instance.user = new UserConcreteComponent("","","", Role.ADMIN);
        }
        return instance;
    }

    public boolean createManager(Manager manager) {
        try {
            // todo: save to db
        } catch (Exception e) {
            System.out.println("Exception creating new manager: " + e.getMessage());
            return false;
        } finally {
            App.getInstance().addUser(manager);
            return true;
        }
    }

    public void deactivateManager(Manager manager) {
        manager.deactivate();
    }

    public History viewHistory(IUser user) { return user.getHistory(); }

    public boolean createTreatmentLocation(TreatmentLocation newLocation) {
        try {
            // todo: save new loc to db
        } catch (Exception e) {
            System.out.println("Exception creating new treatment location " + e.getMessage());
            return false;
        } finally {
            App.getInstance().addTreatmentLocation(newLocation);
            return true;
        }
    }

    public boolean modifyTreatmentLocationName(TreatmentLocation location, String newName) {
        try {
            // todo: save updated loc to db
            location.setName(newName);
        } catch (Exception e) {
            System.out.println("Exception modifying treatment location " + e.getMessage());
            return false;
        } finally {
            return true;
        }
    }

    public boolean modifyTreatmentLocationRoom(TreatmentLocation location, int newRoom) {
        try {
            // todo: save updated loc to db
            location.setCurrentRoom(newRoom);
        } catch (Exception e) {
            System.out.println("Exception modifying treatment location " + e.getMessage());
            return false;
        } finally {
            return true;
        }
    }

    public boolean modifyTreatmentLocationCapacity(TreatmentLocation location, int newCapacity) {
        try {
            // todo: save updated loc to db
            location.setCapacity(newCapacity);
        } catch (Exception e) {
            System.out.println("Exception modifying treatment location " + e.getMessage());
            return false;
        } finally {
            return true;
        }
    }

    public boolean deleteLocation(String code) {
        try {
            // todo: delete loc in db

             /*
             The code block below is for testing only.
             In production, the app should reload the locations
             straight from the database instead of delete element
             from the array
             */
            App app = App.getInstance();
            app.deleteTreatmentLocation(code);
        } catch (Exception e) {
            System.out.println("Exception deleting treatment location " + e.getMessage());
            return false;
        } finally {
            return true;
        }
    }
}
