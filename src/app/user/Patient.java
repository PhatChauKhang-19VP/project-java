package app.user;

import app.util.Location;
import app.util.TreatmentLocation;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Patient extends UserDecorator {
    private int status;
    private Date dob;
    private Location address;
    private TreatmentLocation treatmentLocation;
    private ArrayList<Patient> closeContacts;

    public Patient(
            IUser user,
            int status,
            Date dob,
            Location address,
            TreatmentLocation treatmentLocation,
            ArrayList<Patient> closeContacts
    ) {
        super(user);
        this.status = status;
        this.dob = dob;
        this.address = address;
        this.treatmentLocation = treatmentLocation;

        if (!this.treatmentLocation.addPatient()) {
            this.treatmentLocation = null;
        }

        this.closeContacts = new ArrayList<Patient>(closeContacts);
    }

    public boolean buyPackage() {
        return false;
    }

    public boolean payBill() {
        return false;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        // add to history
        // format date to save update history
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = formatter.format(new Date());
        String content = "Update status: " + this.status  + " -> " + status;
        this.addRecord(date + " " + content);

        // update status and close contacts' status
        this.status = status;
        for (Patient contact : closeContacts) {
            if (contact.status > this.status) {
                contact.setStatus(this.status + 1);
            }
        }
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Location getAddress() {
        return address;
    }

    public void setAddress(Location address) {
        this.address = address;
    }

    public ArrayList<Patient> getCloseContacts() {
        return closeContacts;
    }

    public void setCloseContacts(ArrayList<Patient> closeContacts) {
        this.closeContacts = closeContacts;
    }

    public void addCloseContact(Patient newCloseContact) {
        if (this.status + 1 != newCloseContact.status) {
            newCloseContact.setStatus(this.status + 1);
        }
        this.closeContacts.add(newCloseContact);
    }

    public void showContractedInfo() {
        System.out.print("Username: " + ((UserConcreteComponent)this.user).getUsername());
        System.out.println("\tStatus: F" + status);
    }
    @Override
    public void showInfo() {
        super.showInfo();
        System.out.println("Status: F" + status);
        System.out.println("DOB: " + dob);
        System.out.println("Address: " + address);
        System.out.println("Treatment Location: " + treatmentLocation);
        System.out.println("Close contacts:");
        for (Patient contact : closeContacts) {
            System.out.print("\t");
            contact.showContractedInfo();
        }
    }
}
