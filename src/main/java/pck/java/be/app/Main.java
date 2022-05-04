package pck.java.be.app;

import javafx.util.converter.LocalDateStringConverter;
import pck.java.database.DatabaseCommunication;
import pck.java.be.app.product.Package;
import pck.java.be.app.product.Product;
import pck.java.be.app.user.*;
import pck.java.be.app.util.Location;
import pck.java.be.app.util.TreatmentLocation;

import java.time.LocalDate;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        App app = App.getInstance();
        DatabaseCommunication dbCom = DatabaseCommunication.getInstance();
//        dbCom.loadAll();
//        IUser admin = Admin.getInstance();
//
//        IUser m1 = new Manager(
//                new UserConcreteComponent(
//                        "manager1", "manager1 name", "1", IUser.Role.MANAGER
//                )
//        );
//        Location loc1 = new Location(
//                "123 nhà 1",
//                app.getWardList().get("00001"),
//                app.getDistrictList().get("867"),
//                app.getProvinceList().get("87"));
//        TreatmentLocation tloc1 = new TreatmentLocation("loc1", "Bệnh viện Bệnh Nhiệt đới TP HCM", 1000, 0);
//        IUser p1 = new Patient(
//                new UserConcreteComponent(
//                        "patient1", "patient1 name", "abcd", IUser.Role.PATIENT
//                ),
//                0,
//                LocalDate.parse("2001-06-01"),
//                loc1,
//                tloc1,
//                new ArrayList<>()
//        );

        // test login
//        System.out.println(dbCom.login("admin1", "1"));
//
//        // get manager
//        dbCom.loadAdmin();
//
//        // load administrativeDivision
//        dbCom.loadAdministrativeDivision();
//
//        // load Treatment Locations
//        dbCom.loadTreatmentLocation();
//
//        // load Patients and their close contacts
//        dbCom.loadPatients();
//
//        // load Histories
//        dbCom.loadHistories();
//
//        // load Products and Packages
//        dbCom.loadProductsAndPackages();
//
//        // load Orders
//        dbCom.loadOrders();
//
//        ((Patient) app.getUserList().get("patient1")).payBill();

        /* get manager */
        DatabaseCommunication.getInstance().loadAdmin();

        /* load administrativeDivision */
        DatabaseCommunication.getInstance().loadAdministrativeDivisions();

//        DatabaseCommunication.getInstance().loadTreatmentLocations();

//        app.getUserList().get("patient1").showInfo();
//        App.getInstance().getProductManagement().showInfo();

//        testAddingManager((Manager) m1);
//        testAddingTreatmentLocation();
//        testModifyingTLoc();
//        testDeletingTloc();
//        testViewingPackages((Patient) p1);
//        testSortingPatientList((Manager) m1);
//        testAddingPatient((Manager) m1, (Patient) p1);
//        testFindingProduct((Manager) m1);
//        testAddingPackage((Manager) m1);
//        testDeletingPackage((Manager) m1);

        // Test client server
//        ((Patient) App.getInstance().getUserList().get("patient1")).payBill();
    }

    private static void testAddingManager(Manager manager) {
        Admin.getInstance().createManager(manager);
    }

    private static void testAddingTreatmentLocation() {
        TreatmentLocation tloc = new TreatmentLocation("tloc4", "Treatment Loc 4", 2000, 400);
        Admin.getInstance().createTreatmentLocation(tloc);
    }

    private static void testModifyingTLoc() {
        Admin.getInstance().modifyTreatmentLocationName("tloc4", "Modified Treatment Location 4");
        Admin.getInstance().modifyTreatmentLocationCapacity("tloc4", 2500);
        Admin.getInstance().modifyTreatmentLocationRoom("tloc4", 940);
    }

    private static void testDeletingTloc() {
        Admin.getInstance().deleteLocation("tloc4");
    }

    private static void testViewingPackages(Patient patient) {
        App app = App.getInstance();
        patient.viewPackages();
    }

    private static void testSortingPatientList(Manager manager) {
        ArrayList<String> orders = new ArrayList<>();
        orders.add("f_status");
        manager.sortPatientList(orders);
    }

    private static void testAddingPatient(Manager manager, Patient patient) {
        manager.addPatient(patient);
    }

    private static void testFindingProduct(Manager manager) {
        ArrayList<Product> prods = manager.findProductByName("Su su");
        prods.forEach(product -> {
            System.out.println(product);
        });
    }

    private static void testAddingPackage(Manager manager) {
        Product prod = new Product(
                "product14",
                "Su su",
                "https://res.cloudinary.com/ngo-minh-phat/image/upload/v1647964674/covid_app/products/su-su-tui-500g_lyy8pw.jpg",
                "X",
                10.0);
        Package pkg = new Package(
                "testpackage",
                "Test package",
                "",
                10,
                10,
                200
        );
        pkg.addProduct(prod, 5);
        manager.addPackage(pkg);
    }

    private static void testDeletingPackage(Manager manager) {
        manager.deletePackage("testpackage");
    }
}
