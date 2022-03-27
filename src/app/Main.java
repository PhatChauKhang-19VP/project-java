package app;

import app.database.DatabaseCommunication;
import app.user.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        App app = App.getInstance();
        DatabaseCommunication dbCom = DatabaseCommunication.getInstance();
        IUser admin = Admin.getInstance();

        // test login
        System.out.println(dbCom.login("admin1", "1"));

        // get manager
        dbCom.loadAdmin();

        // load administrativeDivision
        dbCom.loadAdministrativeDivisions();

        // load Treatment Locations
        dbCom.loadTreatmentLocations();

        // load Patients and their close contacts
        dbCom.loadPatients();

        // load Histories
        dbCom.loadHistories();

        // load Products and Packages
        dbCom.loadProductsAndPackages();

        // load Orders
        dbCom.loadOrders();

        ((Patient) app.getUserList().get("patient1")).payBill();

//        for (String key : app.getUserList().keySet()) {
//            app.getUserList().get(key).showInfo();
//        }
    }
}
