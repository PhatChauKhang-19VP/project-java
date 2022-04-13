package pck.java;

import pck.java.be.app.database.DatabaseCommunication;

public class Main {
    public static void main(String[] args) {
        DatabaseCommunication.getInstance().loadAll();
        System.out.println("hello");

        Index.main(args);
    }
}
