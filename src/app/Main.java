package app;

import app.user.*;
import app.util.Location;
import app.util.TreatmentLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        App app = App.getInstance();

        IUser admin = Admin.getInstance();
        admin.setUsername("admin");
        admin.setName("Admin");
        admin.setPassword("*****");

        Location loc1 = new Location("123 Hong Bang","15","5","HCM");

        TreatmentLocation tloc1 = new TreatmentLocation("Benh vien Cho Ray",200,0);
        TreatmentLocation tloc2 = new TreatmentLocation("Benh vien da chien so 1",500,0);

        IUser patient1 = new Patient(
                new UserConcreteComponent("patient1", "Patient 1", "c", IUser.Role.PATIENT),
                0, new Date(2012, 2, 3), loc1, tloc2,
                new ArrayList<Patient>());

        IUser patient2 = new Patient(
                new UserConcreteComponent("patient2", "Patient 2", "d", IUser.Role.PATIENT),
                1, new Date(1992, 12, 7), loc1, tloc1,
                new ArrayList<Patient>());

        IUser patient3 = new Patient(
                new UserConcreteComponent("patient3", "Patient 3", "e", IUser.Role.PATIENT),
                2, new Date(1992, 12, 7), loc1, tloc1, new ArrayList<Patient>());

        IUser patient4 = new Patient(
                new UserConcreteComponent("patient4", "Patient 4", "e", IUser.Role.PATIENT),
                2, new Date(1992, 12, 7), loc1, tloc1,
                new ArrayList<Patient>());

        IUser patient5 = new Patient(
                new UserConcreteComponent("patient5", "Patient 5", "e", IUser.Role.PATIENT),
                3, new Date(1992, 12, 7), loc1, tloc1,
                new ArrayList<Patient>());

        // Test adding component to App object
        app.addUser(admin);
        app.addUser(patient1);
        app.addUser(patient2);
        app.addUser(patient3);
        app.addUser(patient4);
        app.addUser(patient5);
        app.addTreatmentLocation(tloc1);
        app.addTreatmentLocation(tloc2);

        // Test adding manager
        IUser manager1 = new Manager(
                new UserConcreteComponent("manager1", "Manager 1", "b", IUser.Role.MANAGER)
        );
        ((Admin) admin).createManager((Manager)manager1);

        // Test creating and updating treatment location
        // via admin
        ((Admin) admin).createTreatmentLocation(new TreatmentLocation("Benh vien PNT",500,0));
        ((Admin) admin).modifyTreatmentLocationName(tloc1, "Benh vien Hung Vuong");
        ((Admin) admin).deleteLocation(tloc1);
        //app.showTreatmentLocationList();

        // Test handling patient and F-system
        ((Patient) patient1).addCloseContact((Patient) patient2);
        ((Patient) patient2).addCloseContact((Patient) patient3);
        ((Patient) patient2).addCloseContact((Patient) patient4);
        ((Patient) patient3).addCloseContact((Patient) patient5);

        ((Patient) patient3).setStatus(0);
        ((Patient) patient2).setStatus(0);

        //!! test manager
        //* 1.2.1
        ((Manager) manager1).showPatientList();
        ((Manager) manager1).showPatientInfo(patient1);


        Patient pTemp = ((Manager) manager1).findPatientWithId("patient3");
        System.out.println(patient3);
        System.out.println(pTemp);

        ((Patient) patient5).payBill();

//        for (IUser i : app.getUserList()) {
//            i.showInfo();
//            System.out.println("\n\n=======\n");
//        }
    }
}
