package app;

import app.user.Admin;
import app.user.IUser;
import app.user.UserConcreteComponent;
import app.user.UserDecorator;
import app.util.Location;

import java.util.ArrayList;

public class App {
    private static App instance = null;
    private ArrayList<IUser> userList;
    private ArrayList<Location> locationList;

    private App() {
        userList = new ArrayList<IUser>();
        locationList = new ArrayList<Location>();
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

    public void setLocationList(ArrayList<Location> locationList) {
        this.locationList = locationList;
    }

    public ArrayList<Location> getLocationList() {
        return locationList;
    }
}