package pck.java.be.app.user;

import pck.java.be.app.database.DatabaseCommunication;
import pck.java.be.app.database.DeleteQuery;
import pck.java.be.app.database.InsertQuery;
import pck.java.be.app.database.UpdateQuery;
import pck.java.be.app.util.History;
import pck.java.be.app.util.TreatmentLocation;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Admin decorator, also implemented with Singleton pattern.
 */
public class Admin extends UserDecorator {

    private static Admin instance = null;

    private Admin() {
    }

    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
            instance.user = new UserConcreteComponent("", "", "", Role.ADMIN);
        }
        return instance;
    }

    private boolean createLoginInfo(IUser user) {
        try {
            InsertQuery insertQuery = new InsertQuery();
            ArrayList<String> loginInfoColumns = new ArrayList<>() {
                {
                    add("username");
                    add("password");
                    add("account_status");
                    add("user_type");
                }
            };
            ArrayList<String> loginInfo = new ArrayList<>() {
                {
                    add("'" + user.getUsername() + "'");
                    add("'" + user.getPassword() + "'");
                    add("'ACTIVE'");
                    add("'MANAGER'");
                }
            };
            insertQuery.insertInto("LOGIN_INFOS")
                    .columns(loginInfoColumns)
                    .values(loginInfo);
            DatabaseCommunication.getInstance().execute(insertQuery.getQuery());
        } catch (Exception e) {
            System.out.println("Exception creating manager login info: " + e.getMessage());
            return false;
        } finally {
            return true;
        }
    }

    public boolean createManager(Manager manager) {
        try {
            createLoginInfo(manager);
            InsertQuery insertQuery = new InsertQuery();
            ArrayList<String> values = new ArrayList<>();
            values.add("'" + manager.getUsername() + "'");
            values.add("'" + manager.getName() + "'");
            insertQuery.insertInto("MANAGERS")
                    .columns("username")
                    .columns("name")
                    .values(values);
            DatabaseCommunication.getInstance().execute(insertQuery.getQuery());
        } catch (Exception e) {
            System.out.println("Exception creating new manager: " + e.getMessage());
            return false;
        } finally {
            return true;
        }
    }

    public void deactivateManager(Manager manager) {
        manager.deactivate();
        UpdateQuery updateQuery = new UpdateQuery();
        updateQuery.update("MANAGER").set("status", "INACTIVE");
        try {
            DatabaseCommunication.getInstance().execute(updateQuery.getQuery());
        } catch (SQLException e) {
            System.out.println("Exception deactivating manager: " + e.getMessage());
        }
    }

    public History viewHistory(IUser user) {
        return user.getHistory();
    }

    public boolean createTreatmentLocation(TreatmentLocation newLocation) {
        try {
            InsertQuery insertQuery = new InsertQuery();
            ArrayList<String> columns = new ArrayList<>() {
                {
                    add("treatment_location_code");
                    add("name");
                    add("capacity");
                    add("current_room");
                }
            };
            ArrayList<String> values = new ArrayList<>() {
                {
                    add("'" + newLocation.getCode() + "'");
                    add("'" + newLocation.getName() + "'");
                    add(String.valueOf(newLocation.getCapacity()));
                    add(String.valueOf(newLocation.getCurrentRoom()));
                }
            };
            insertQuery.insertInto("TREATMENT_LOCATIONS")
                    .columns(columns)
                    .values(values);
            DatabaseCommunication.getInstance().execute(insertQuery.getQuery());
        } catch (Exception e) {
            System.out.println("Exception creating new treatment location " + e.getMessage());
            return false;
        } finally {
            // todo: re-load treatment location from db
            return true;
        }
    }

    public boolean modifyTreatmentLocationName(String locationID, String newName) {
        try {
            UpdateQuery updateQuery = new UpdateQuery();
            updateQuery.update("TREATMENT_LOCATIONS")
                    .set("name", "'" + newName + "'")
                    .where("treatment_location_code='" + locationID + "'");
            DatabaseCommunication.getInstance().execute(updateQuery.getQuery());
        } catch (Exception e) {
            System.out.println("Exception modifying treatment location: " + e.getMessage());
            return false;
        } finally {
            return true;
        }
    }

    public boolean modifyTreatmentLocationRoom(String locationID, int newRoom) {
        try {
            UpdateQuery updateQuery = new UpdateQuery();
            updateQuery.update("TREATMENT_LOCATIONS")
                    .set("current_room", String.valueOf(newRoom))
                    .where("treatment_location_code='" + locationID + "'");
            DatabaseCommunication.getInstance().execute(updateQuery.getQuery());
        } catch (Exception e) {
            System.out.println("Exception modifying treatment location: " + e.getMessage());
            return false;
        } finally {
            return true;
        }
    }

    public boolean modifyTreatmentLocationCapacity(String locationID, int newCapacity) {
        try {
            UpdateQuery updateQuery = new UpdateQuery();
            updateQuery.update("TREATMENT_LOCATIONS")
                    .set("capacity", String.valueOf(newCapacity))
                    .where("treatment_location_code='" + locationID + "'");
            DatabaseCommunication.getInstance().execute(updateQuery.getQuery());
        } catch (Exception e) {
            System.out.println("Exception modifying treatment location: " + e.getMessage());
            return false;
        } finally {
            return true;
        }
    }

    public boolean deleteLocation(String locationID) {
        try {
            DeleteQuery deleteQuery = new DeleteQuery();
            deleteQuery.deleteFrom("TREATMENT_LOCATIONS")
                    .where("treatment_location_code='" + locationID + "'");
            DatabaseCommunication.getInstance().execute(deleteQuery.getQuery());
            System.out.println(deleteQuery.getQuery());
        } catch (Exception e) {
            System.out.println("Exception deleting treatment location " + e.getMessage());
            return false;
        } finally {
            return true;
        }
    }
}
