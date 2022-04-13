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

    opens pck.java to javafx.fxml;
    exports pck.java;

    opens pck.java.fe.mainPage to javafx.fxml;
    exports pck.java.fe.mainPage;

    opens pck.java.fe.patient to javafx.fxml;
    exports pck.java.fe.patient;
}