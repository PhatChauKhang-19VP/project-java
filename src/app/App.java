package app;

import app.user.IUser;
import app.util.Location;
import app.util.TreatmentLocation;

import java.util.ArrayList;

/** App controller, implemented according to Singleton pattern. */
public class App {
    private static App instance = null;
    private ArrayList<IUser> userList;
    private ArrayList<TreatmentLocation> treatmentLocationList;

    private App() {
        userList = new ArrayList<IUser>();
        treatmentLocationList = new ArrayList<TreatmentLocation>();
    }

    public static App getInstance() {
        if (instance == null)
            instance = new App();
        return instance;
    }

    public void setUserList(ArrayList<IUser> userList) {
        this.userList = userList;
    }

    public ArrayList<IUser> getUserList() {
        return userList;
    }

    public void addUser(IUser newUser) {
        userList.add(newUser);
    }

    public boolean deleteUser(IUser user) {
        if (user.getRole() == IUser.Role.ADMIN) {
            return false;
        } else {
            userList.remove(user);
            return true;
        }
    }

    public void setTreatmentLocations(ArrayList<TreatmentLocation> treatmentLocationList) {
        this.treatmentLocationList = treatmentLocationList;
    }

    public ArrayList<TreatmentLocation> getTreatmentLocations() {
        return treatmentLocationList;
    }

    public void addTreatmentLocation(TreatmentLocation newLocation) {
        this.treatmentLocationList.add(newLocation);
    }

    public boolean deleteTreatmentLocation(TreatmentLocation location) {
        try {
            treatmentLocationList.remove(location);
        } catch (Exception e) {
            System.out.println("Exception deleting treatment location: " + e.getMessage());
        } finally {
            return true;
        }
    }

    public void showTreatmentLocationList() {
        for (TreatmentLocation loc : treatmentLocationList) {
            System.out.println(loc);
        }
    }
}