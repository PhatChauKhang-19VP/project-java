package pck.java.be.app.database;

import pck.java.be.app.App;

public class Main {
    public static void main(String[] args) {
        if (DatabaseCommunication.getInstance().loadAll()) {
            System.out.println("Database Import Successfully");

            App app = App.getInstance();

            for (String key : app.getUserList().keySet()) {
                app.getUserList().get(key).showInfo();
            }
        } else {
            System.out.println("FAILED");
        }
    }
}
