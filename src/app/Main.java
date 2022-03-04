package app;

import app.user.*;
import app.util.Location;
import app.util.TreatmentLocation;

import java.util.ArrayList;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        App app = App.getInstance();

        Location loc1 = new Location(
                "123 Hong Bang",
                "15",
                "5",
                "HCM"
        );

        TreatmentLocation tloc1 = new TreatmentLocation(
                "56 An Duong Vuong",
                "4",
                "5",
                "HCM",
                "1",
                200,
                0
        );

        TreatmentLocation tloc2 = new TreatmentLocation(
                "32 Hung Vuong",
                "4",
                "5",
                "HCM",
                "2",
                500,
                0
        );

        IUser admin1 = new Admin(
                new UserConcreteComponent("admin1", "Admin 1", "a", IUser.Role.ADMIN)
        );

        IUser admin2 = new Admin(
                new UserConcreteComponent("admin2", "Admin 2", "b", IUser.Role.ADMIN)
        );

        IUser patient1 = new Patient(
                new UserConcreteComponent("patient1", "Patient 1", "c", IUser.Role.PATIENT),
                0,
                new Date(2012, 2, 3),
                loc1,
                tloc2,
                new ArrayList<Patient>());

        IUser patient2 = new Patient(
                new UserConcreteComponent("patient2", "Patient 2", "d", IUser.Role.PATIENT),
                1,
                new Date(1992, 12, 7),
                loc1,
                tloc1,
                new ArrayList<Patient>());

        IUser patient3 = new Patient(
                new UserConcreteComponent("patient3", "Patient 3", "e", IUser.Role.PATIENT),
                2,
                new Date(1992, 12, 7),
                loc1,
                tloc1,
                new ArrayList<Patient>());

        IUser patient4 = new Patient(
                new UserConcreteComponent("patient4", "Patient 4", "e", IUser.Role.PATIENT),
                2,
                new Date(1992, 12, 7),
                loc1,
                tloc1,
                new ArrayList<Patient>());

        IUser patient5 = new Patient(
                new UserConcreteComponent("patient5", "Patient 5", "e", IUser.Role.PATIENT),
                3,
                new Date(1992, 12, 7),
                loc1,
                tloc1,
                new ArrayList<Patient>());

        ((Patient) patient1).addCloseContact((Patient) patient2);
        ((Patient) patient2).addCloseContact((Patient) patient3);
        ((Patient) patient2).addCloseContact((Patient) patient4);
        ((Patient) patient3).addCloseContact((Patient) patient5);

        ((Patient) patient3).setStatus(0);
        ((Patient) patient2).setStatus(0);

        // Testing
        app.addUser(admin1);
        app.addUser(admin2);
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

        ((Admin) admin1).createManager((Manager)manager1);

        System.out.println(((Admin) admin1).viewHistory(patient3));

//        for (IUser i : app.getUserList()) {
//            i.showInfo();
//            System.out.println("\n\n=======\n");
//        }
    }
}
