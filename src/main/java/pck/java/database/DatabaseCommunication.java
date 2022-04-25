package pck.java.database;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import pck.java.be.app.App;
import pck.java.be.app.product.Order;
import pck.java.be.app.product.Package;
import pck.java.be.app.product.Product;
import pck.java.be.app.user.*;
import pck.java.be.app.util.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseCommunication {
    private static DatabaseCommunication instance;
    private Connection conn;
    private Statement stmt;
    private static final String user = "sa";
    private static final String password = "1";

    private DatabaseCommunication() {
    }

    public static DatabaseCommunication getInstance() {
        if (instance == null) {
            instance = new DatabaseCommunication();
        }

        return instance;
    }

    public boolean loadAll() {
        DatabaseCommunication dbCom = DatabaseCommunication.getInstance();
        return dbCom.loadAdmin()
                && dbCom.loadAdministrativeDivisions()
                && dbCom.loadTreatmentLocations()
                && dbCom.loadPatients()
                && dbCom.loadManagers()
                && dbCom.loadProductsAndPackages()
                && dbCom.loadHistories()
                && dbCom.loadOrders();
    }

    public boolean loadAdmin() {
        try {
            SelectQuery selectAdmin = new SelectQuery();
            selectAdmin
                    .select("l.username as 'username'")
                    .select("l.password as 'password'")
                    .select("a.name as 'name'")
                    .from("admins as a join login_infos as l on a.username = l.username");

            List<Map<String, Object>> rs = executeQuery(selectAdmin.getQuery());


            rs.forEach((map) -> {
                Admin.getInstance().setUsername((String) map.get("username"));
                Admin.getInstance().setPassword((String) map.get("password"));
                Admin.getInstance().setName((String) map.get("name"));
            });


            App.getInstance().addUser(Admin.getInstance());

            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    public boolean loadAdministrativeDivisions() {
        boolean returnValue = false;

        try {
            // load Provinces
            SelectQuery selectProvinces = new SelectQuery();
            selectProvinces
                    .select("*")
                    .from("PROVINCES");

            List<Map<String, Object>> rs = executeQuery(selectProvinces.getQuery());
            HashMap<String, Province> provinceList = new HashMap<>();

            rs.forEach(map -> {
                String code = String.valueOf(map.get("code")),
                        name = String.valueOf(map.get("full_name")),
                        name_en = (String) map.get("full_name_en");

                provinceList.put(code, new Province(code, name, name_en));
            });

            App.getInstance().setProvinceList(provinceList);

            // get district
            SelectQuery selectDistrict = new SelectQuery();
            selectDistrict
                    .select("*")
                    .from("DISTRICTS");

            rs = executeQuery(selectDistrict.getQuery());
            HashMap<String, District> districtList = new HashMap<>();
            rs.forEach(map -> {
                String code = String.valueOf(map.get("code")),
                        name = String.valueOf(map.get("full_name")),
                        name_en = String.valueOf(map.get("full_name_en")),
                        province_code = (String) map.get("province_code");
                districtList.put(code, new District(code, name, name_en, provinceList.get(province_code)));
            });
            App.getInstance().setDistrictList(districtList);

            // get wards
            SelectQuery selectWards = new SelectQuery();
            selectWards
                    .select("*")
                    .from("WARDS");

            rs = executeQuery(selectWards.getQuery());
            HashMap<String, Ward> wardList = new HashMap<>();

            rs.forEach(map -> {
                String code = String.valueOf(map.get("code")),
                        name = String.valueOf(map.get("full_name")),
                        name_en = String.valueOf(map.get("full_name_en")),
                        district_code = (String) map.get("district_code");
                wardList.put(code, new Ward(code, name, name_en, districtList.get(district_code)));
            });
            App.getInstance().setWardList(wardList);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    public boolean loadTreatmentLocations() {
        try {
            // get treatment location
            SelectQuery selectTreatmentLocations = new SelectQuery();
            selectTreatmentLocations
                    .select("*")
                    .from("TREATMENT_LOCATIONS");

            List<Map<String, Object>> rs = executeQuery(selectTreatmentLocations.getQuery());
            rs.forEach(map -> {
                String code = String.valueOf(map.get("treatment_location_code")),
                        name = (String) map.get("name");
                int capacity = (int) map.get("capacity"),
                        current_room = (int) map.get("current_room");

                App.getInstance().addTreatmentLocation(new TreatmentLocation(code, name, capacity, current_room));
            });

            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    public boolean loadPatients() {
        try {
            SelectQuery selectPatients = new SelectQuery();
            selectPatients
                    .select("l.username as 'username'")
                    .select("l.password as 'password'")
                    .select("p.name as 'name'")
                    .select("p.f_status  as 'status'")
                    .select("p.date_of_birth as 'dob'")
                    .select("p.address_province_code as 'pcode'")
                    .select("p.address_district_code as 'dcode'")
                    .select("p.address_ward_code as 'wcode'")
                    .select("p.address_line as 'al'")
                    .select("p.treatment_location_code as 'tloc_code'")
                    .from("PATIENTS as p join LOGIN_INFOS as l on p.username = l.username");

            List<Map<String, Object>> rs = executeQuery(selectPatients.getQuery());

            rs.forEach(map -> {
                String username = String.valueOf(map.get("username")),
                        password = String.valueOf(map.get("password")),
                        name = String.valueOf(map.get("name")),
                        p_code = String.valueOf(map.get("pcode")),
                        d_code = String.valueOf(map.get("dcode")),
                        w_code = String.valueOf(map.get("wcode")),
                        address_line = String.valueOf(map.get("al")),
                        tloc_code = (String) map.get("tloc_code");
                int status = (int) map.get("status");
                java.sql.Date dob = (Date) map.get("dob");

                App.getInstance().addUser(new Patient(
                        new UserConcreteComponent(username, name, password, IUser.Role.PATIENT),
                        status,
                        dob.toLocalDate(),
                        new Location(address_line,
                                App.getInstance().getWardList().get(w_code),
                                App.getInstance().getDistrictList().get(d_code),
                                App.getInstance().getProvinceList().get(p_code)),
                        App.getInstance().getTreatmentLocationList().get(tloc_code),
                        new ArrayList<>()
                ));
            });

            // load close contacts
            SelectQuery selectCloseContacts = new SelectQuery();
            selectCloseContacts
                    .select("*")
                    .from("CLOSE_CONTACTS");
            rs = executeQuery(selectCloseContacts.getQuery());

            rs.forEach(map -> {
                String f_upper_username = String.valueOf(map.get("f_upper_username")),
                        f_lower_username = (String) map.get("f_lower_username");

                ((Patient) App.getInstance()
                        .getUserList()
                        .get(f_upper_username))
                        .addCloseContact((Patient) App
                                .getInstance()
                                .getUserList()
                                .get(f_lower_username));
            });

            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    public boolean loadManagers() {
        try {
            SelectQuery selectManagers = new SelectQuery();
            selectManagers
                    .select("l.username as 'username'")
                    .select("l.password as 'password'")
                    .select("a.name as 'name'")
                    .from("MANAGERS as a join login_infos as l on a.username = l.username");

            List<Map<String, Object>> rs = executeQuery(selectManagers.getQuery());


            rs.forEach((map) -> {
                String username = (String) map.get("username"),
                        password = (String) map.get("password"),
                        name = (String) map.get("name");

                Manager manager = new Manager(
                        new UserConcreteComponent(
                                username, name, password, IUser.Role.MANAGER
                        )
                );
                App.getInstance().addUser(manager);
            });

            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    public boolean loadProductsAndPackages() {
        // load products
        try {
            SelectQuery selectProducts = new SelectQuery();
            selectProducts
                    .select("*")
                    .from("PRODUCTS");
            List<Map<String, Object>> rs = executeQuery(selectProducts.getQuery());

            rs.forEach(map -> {
                String id = String.valueOf(map.get("product_id")),
                        name = String.valueOf(map.get("name")),
                        img_src = String.valueOf(map.get("img_src")),
                        unit = String.valueOf(map.get("unit"));
                double price = (double) map.get("price");

                App.getInstance()
                        .getProductManagement()
                        .addProduct(new Product(id, name, img_src, unit, price));

            });

            SelectQuery selectPackages = new SelectQuery();
            selectPackages
                    .select("*")
                    .from("PACKAGES");
            rs = executeQuery(selectPackages.getQuery());

            rs.forEach(map -> {
                String id = String.valueOf(map.get("package_id")),
                        name = String.valueOf(map.get("name")),
                        img_src = String.valueOf(map.get("img_src"));
                int purchased_amount_limit = (int) map.get("purchased_amount_limit"),
                        time_limit = (int) map.get("time_limit");
                double price = (double) map.get("price");


                App.getInstance().getProductManagement().addPackage(new Package(id, name, img_src, purchased_amount_limit, time_limit, price, new HashMap<>()));

            });

            // load products_in_packages
            SelectQuery selectProdInPkg = new SelectQuery();
            selectProdInPkg
                    .select("*")
                    .from("PRODUCTS_IN_PACKAGES");
            rs = executeQuery(selectProdInPkg.getQuery());

            rs.forEach(map -> {
                String product_id = String.valueOf(map.get("product_id")),
                        package_id = String.valueOf(map.get("package_id"));
                int quantity = (int) map.get("quantity");

                App.getInstance().getProductManagement().addProductToPackage(
                        package_id,
                        App.getInstance().getProductManagement().getProductList().get(product_id),
                        quantity);
            });

            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    public boolean loadHistories() {
        try {
            // load HISTORIES
            SelectQuery selectHistories = new SelectQuery();
            selectHistories
                    .select("*")
                    .from("HISTORIES");

            List<Map<String, Object>> rs = executeQuery(selectHistories.getQuery());

            rs.forEach(map -> {
                String belong_to_username = String.valueOf(map.get("belong_to_username")),
                        history_content = (String) map.get("history_content");

                java.sql.Timestamp timestamp = (Timestamp) map.get("at_datetime");
                App.getInstance()
                        .getUserList()
                        .get(belong_to_username)
                        .addRecord("[" + timestamp.toString() + "]" + history_content);
            });

            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    public boolean loadOrders() {
        try {
            // load ORDERS
            SelectQuery selectOrders = new SelectQuery();
            selectOrders
                    .select("*")
                    .from("ORDERS");
            List<Map<String, Object>> rs = executeQuery(selectOrders.getQuery());

            HashMap<String, Order> orderList = new HashMap<>();

            rs.forEach(map -> {
                String order_id = (String) map.get("order_id");
                String belong_to_username = String.valueOf(map.get("belong_to_username"));
                java.sql.Timestamp timestamp = (Timestamp) map.get("at_datetime");
                LocalDateTime date = timestamp.toLocalDateTime();
                double total = (double) map.get("total");

                Order order = new Order(order_id,
                        App.getInstance().getUserList().get(belong_to_username),
                        date,
                        new HashMap<>(),
                        total);
                orderList.put(order_id, order);
            });
            App.getInstance().getProductManagement().setOrderList(orderList);

            // load ORDERS_DETAIL
            SelectQuery selectOrderDetails = new SelectQuery();
            selectOrderDetails
                    .select("*")
                    .from("ORDERS_DETAILS");

            rs = executeQuery(selectOrderDetails.getQuery());

            rs.forEach(map -> {
                String order_id = (String) map.get("order_id");
                int quantity = (int) map.get("quantity");
                String package_id = String.valueOf(map.get("package_id"));
                double price = (double) map.get("price"),
                        subTotal = (double) map.get("sub_total");

                App.getInstance()
                        .getProductManagement()
                        .getOrderList()
                        .get(order_id)
                        .savePackageDetail(
                                App.getInstance().getProductManagement().getPackageList().get(package_id),
                                quantity
                        );
            });

            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    private void open() {
        try {
            SQLServerDataSource sqlDs = new SQLServerDataSource();

            sqlDs.setIntegratedSecurity(false);
            sqlDs.setEncrypt(false);

            sqlDs.setUser(user);
            sqlDs.setPassword(password);

            sqlDs.setServerName("localhost");
            sqlDs.setPortNumber(1433);
            sqlDs.setDatabaseName("db_covid_app");

            conn = sqlDs.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error closing connection.");
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> executeQuery(String sql) throws SQLException {
        open();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> row = null;
        ResultSetMetaData metaData = rs.getMetaData();
        Integer columnCount = metaData.getColumnCount();

        while (rs.next()) {
            row = new HashMap<String, Object>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(metaData.getColumnName(i), rs.getObject(i));
            }
            resultList.add(row);
        }
        rs.close();
        stmt.close();
        return resultList;
    }

    public void execute(String sql) throws SQLException {
        open();
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        stmt.close();
        close();
    }

    public void printResult(List<Map<String, Object>> rs) throws SQLException {
        // Prepare metadata object and get the number of columns.
        if (rs.size() == 0) {
            return;
        }

        for (String key : rs.get(0).keySet()) {
            System.out.print(key + " | ");
        }

        System.out.println();
        for (Map<String, Object> item : rs) {
            item.entrySet().forEach(entry -> {
                System.out.print(entry.getValue() + " | ");
            });
            System.out.println();
        }
    }

}
