package app.user;

import app.util.Location;

import java.util.ArrayList;
import java.util.Date;

public class Patient extends UserDecorator {
    private int status;
    private Date dob;
    private Location address;
    private ArrayList<Patient> closeContacts;

    public Patient(IUser user) {
        super(user);
    }

    public boolean buyPackage() {
        return false;
    }

    public boolean payBill() {
        return false;
    }

    @Override
    public void showInfo() {
        super.showInfo();
        System.out.println("Status: F" + status);
        System.out.println("DOB: " + dob);
        System.out.println("Address: " + address);
        System.out.println("Close contacts:");
        for (Patient contact : closeContacts) {
            contact.showInfo();
        }
    }

    public boolean modifyLocation() {
        return false;
    }

    public boolean deleteLocation() {
        return false;
    }
}
