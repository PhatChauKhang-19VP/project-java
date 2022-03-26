package app;

import app.database.DatabaseCommunication;
import app.product.Order;
import app.product.Package;
import app.product.Product;
import app.user.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import app.util.*;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        App app = App.getInstance();
        IUser admin = Admin.getInstance();
        // get manager
        DatabaseCommunication.getInstance().loadAdmin();

        // load administrativeDivision
        DatabaseCommunication.getInstance().loadAdministrativeDivision();

        for (String key : app.getUserList().keySet()){
            app.getUserList().get(key).showInfo();
        }
    }
}
