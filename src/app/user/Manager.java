package app.user;

import app.App;
import app.util.Location;

import java.util.Comparator;

public class Manager extends UserDecorator {
    public Manager(IUser user) {
        super(user);
    }

    void showF0(){
        // todo: get F0 list from database and show it

        // OR
        for (IUser user : App.getInstance().getUserList()){
            if (user.getRole() == Role.PATIENT){
                System.out.println(user);
            }
        }
    }

    void showCloseContacts(){
        for (IUser user : App.getInstance().getUserList()){
            if (user.getRole() == Role.PATIENT){
                user.showInfo();
            }
        }
    }

    public Patient findPatientWithId(String id){
        return (Patient)App.getInstance().getUserList().get(0);
    }

    void sortPatient(Comparator comparator){
        // sort by comparator
    }

    void sortPatient(/*order by*/){
        return;
    }

    boolean addPatient(Patient newPatient){
        try {
            // todo: add to database
        }
        catch (Exception e){
            return false;
        }
        finally {
            App.getInstance().addUser(newPatient);
            return true;
        }
    }

    boolean updateStatus(Patient patient, int newStatus) {
        patient.setStatus(newStatus);
        return true;
    }

    boolean updateTreatmentLocation(String patientId, String locationId){
        App app = App.getInstance();
        // return app.getTreatmentLocation().findById(id).add(findPatientWithId(id))

        return true;
    }
}