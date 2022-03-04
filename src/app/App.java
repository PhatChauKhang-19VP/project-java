package app;

import app.user.IUser;
import app.util.Location;
import app.util.TreatmentLocation;

import java.util.ArrayList;

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

    public void addUser(IUser newUser) {
        userList.add(newUser);
    }

    public ArrayList<IUser> getUserList() {
        return userList;
    }

    public void setTreatmentLocations(ArrayList<TreatmentLocation> treatmentLocationList) {
        this.treatmentLocationList = treatmentLocationList;
    }

    public void addTreatmentLocation(TreatmentLocation newLocation) {
        this.treatmentLocationList.add(newLocation);
    }

    public ArrayList<TreatmentLocation> getTreatmentLocations() {
        return treatmentLocationList;
    }
}