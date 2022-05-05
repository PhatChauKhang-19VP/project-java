module pck.java {
    requires java.naming;

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires mssql.jdbc;
    requires AnimateFX;
    requires cloudinary.http42;
    requires cloudinary.core;

    opens pck.java to javafx.base;
    exports pck.java;

    opens pck.java.be.app.util to javafx.base;

    exports pck.java.be.app.util;

    opens pck.java.be.app.user to javafx.base;
    exports pck.java.be.app.user;

    opens pck.java.be.app.product to javafx.base;
    exports pck.java.be.app.product;

    opens pck.java.fe.mainPage to javafx.fxml;
    exports pck.java.fe.mainPage;

    opens pck.java.fe.admin to javafx.fxml;
    exports pck.java.fe.admin;

    opens pck.java.fe.manager to javafx.fxml;
    exports pck.java.fe.manager;

    opens pck.java.fe.patient to javafx.fxml;
    exports pck.java.fe.patient;

    opens pck.java.fe.manager.modal;
    exports pck.java.fe.manager.modal to javafx.fxml;
}