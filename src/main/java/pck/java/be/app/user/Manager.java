package pck.java.be.app.user;

import pck.java.be.app.App;
import pck.java.database.*;
import pck.java.be.app.product.Package;
import pck.java.be.app.product.Product;
import pck.java.be.app.util.Location;
import pck.java.be.app.util.Pair;
import pck.java.be.app.util.TreatmentLocation;
import pck.java.database.*;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Manager extends UserDecorator {
    public Manager(IUser user) {
        super(user);
    }

    //! 1.2.1 Patient management
    public void showPatientList() {
        if (getRole() == Role.MANAGER) {
            App.getInstance().getUserList().forEach((key, user) -> {
                if (user.getRole() == Role.PATIENT) {
                    user.showInfo();
                }
            });
        }
    }

    public void showPatientInfo(IUser patient) {
        if (getRole() == Role.MANAGER) {
            if (patient.getRole() == Role.PATIENT) {
                patient.showInfo();
            } else {
                System.out.println("not a patient");
            }
        } else {
            System.out.println("You are not the manager");
        }
    }

    public void sortPatientList(ArrayList<String> orders) {
        if (getRole() == Role.MANAGER) {
            SelectQuery selectQuery = new SelectQuery();
            selectQuery.from("PATIENTS").select("*").orderBy(orders);
            List<Map<String, Object>> rs;
            try {
                rs = DatabaseCommunication.getInstance().executeQuery(selectQuery.getQuery());
                DatabaseCommunication.getInstance().printResult(rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean createLoginInfo(IUser user) {
        try {
            InsertQuery insertQuery = new InsertQuery();
            ArrayList<String> loginInfoColumns = new ArrayList<>() {
                {
                    add("username");
                    add("password");
                    add("account_status");
                    add("user_type");
                }
            };
            ArrayList<String> loginInfo = new ArrayList<>() {
                {
                    add("'" + user.getUsername() + "'");
                    add("HASHBYTES('SHA2_512'," + "'" + user.getPassword() + "')");
                    add("'ACTIVE'");
                    add("'PATIENT'");
                }
            };
            insertQuery.insertInto("LOGIN_INFOS")
                    .columns(loginInfoColumns)
                    .values(loginInfo);
            DatabaseCommunication.getInstance().execute(insertQuery.getQuery());

            return true;
        } catch (Exception e) {
            System.out.println("Exception creating manager login info: " + e.getMessage());
            return false;
        }
    }

    //! 1.2.2 add covid patient
    public boolean addPatient(Patient newPatient) {
        if (getRole() == Role.MANAGER) {
            createLoginInfo(newPatient);
            InsertQuery insertQuery = new InsertQuery();
            ArrayList<String> columns = new ArrayList<>() {
                {
                    add("username");
                    add("name");
                    add("f_status");
                    add("date_of_birth");
                    add("address_province_code");
                    add("address_district_code");
                    add("address_ward_code");
                    add("address_line");
                    add("treatment_location_code");
                    add("debit_balance");
                }
            };
            ArrayList<String> values = new ArrayList<>() {
                {
                    add("'" + newPatient.getUsername() + "'");
                    add("N'" + newPatient.getName() + "'");
                    add(String.valueOf(newPatient.getStatus()));
                    add("'" + java.sql.Date.valueOf(newPatient.getDob()) + "'");
                    add("N'" + newPatient.getAddress().getProvince().getCode() + "'");
                    add("N'" + newPatient.getAddress().getDistrict().getCode() + "'");
                    add("N'" + newPatient.getAddress().getWard().getCode() + "'");
                    add("N'" + newPatient.getAddress().getAddressLine() + "'");
                    add("'" + newPatient.getTreatmentLocation().getCode() + "'");
                    add("0");
                }
            };
            insertQuery.insertInto("PATIENTS").columns(columns).values(values);

            addToTreatmentLocation(newPatient);
            createBankAccount(newPatient);

            try {
                DatabaseCommunication.getInstance().execute(insertQuery.getQuery());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean addCloseContact(Patient p1, Patient p2) {
        if (getRole() == Role.MANAGER) {
            InsertQuery insertQuery = new InsertQuery();
            ArrayList<String> columns = new ArrayList<>() {
                {
                    add("f_upper_username");
                    add("f_lower_username");
                }
            };
            ArrayList<String> values = new ArrayList<>() {
                {
                    add("'" + p1.getUsername() + "'");
                    add("'" + p2.getUsername() + "'");
                }
            };
            insertQuery.insertInto("CLOSE_CONTACTS").columns(columns).values(values);

            // insert close contact
            try {
                DatabaseCommunication.getInstance().execute(insertQuery.getQuery());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean addToTreatmentLocation(Patient p) {
        if (getRole() == Role.MANAGER) {
            String query = "";
            query += "declare @tloc_code varchar(20) = '" + p.getTreatmentLocation().getCode() + "'\n";
            query += "declare @old_room int = (select t.current_room from TREATMENT_LOCATIONS as t where t.treatment_location_code=@tloc_code)\n";
            query += """
                    update TREATMENT_LOCATIONS
                        set current_room = @old_room + 1    
                        where treatment_location_code=@tloc_code""";

            System.out.println(query);
            try {
                DatabaseCommunication.getInstance().execute(query);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean createBankAccount(Patient p) {
        if (getRole() == Role.MANAGER) {
            InsertQuery iq = new InsertQuery();
            iq.insertInto("BANK_ACCOUNTS")
                    .columns("password, belong_to_username, balance, created_at, minimum_payment")
                    .values(new ArrayList<>(Arrays.asList(
                            "HASHBYTES('SHA2_512', '" + p.getPassword() + "')",
                            "'" + p.getUsername() + "'",
                            "'15000000'",
                            "'" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + "'",
                            "100")));

            System.out.println(iq.getQuery());
            try {
                DatabaseCommunication.getInstance().execute(iq.getQuery());
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    //! 1.2.3 update patient status
    public boolean updateStatus(Patient patient, int newStatus) {
        if (getRole() == Role.MANAGER) {
            patient.setStatus(newStatus);
            return true;
        }
        return false;
    }

    public boolean updateTloc(Patient patient, TreatmentLocation tloc) {
        if (getRole() == Role.MANAGER) {
            if (tloc.getCurrentRoom() >= tloc.getCapacity()){
                return false;
            }

            try {
                //decrease old curr room by 1
                String query = "";
                query += "declare @tloc_code varchar(20) = '" + patient.getTreatmentLocation().getCode() + "'\n";
                query += "declare @old_room int = (select t.current_room from TREATMENT_LOCATIONS as t where t.treatment_location_code=@tloc_code)\n";
                query += """
                    update TREATMENT_LOCATIONS
                        set current_room = @old_room - 1    
                        where treatment_location_code=@tloc_code""";
                DatabaseCommunication.getInstance().execute(query);

                //update tloc
                patient.setTreatmentLocation(tloc);
                query = "";
                query += "declare @tloc_code varchar(20) = '" + patient.getTreatmentLocation().getCode() + "'\n";
                query += "declare @old_room int = (select t.current_room from TREATMENT_LOCATIONS as t where t.treatment_location_code=@tloc_code)\n";
                query += """
                    update TREATMENT_LOCATIONS
                        set current_room = @old_room + 1   
                        where treatment_location_code=@tloc_code""";
                DatabaseCommunication.getInstance().execute(query);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean updatePatient(Patient patient){
        if (getRole() == Role.MANAGER) {

            UpdateQuery uq = new UpdateQuery();
            String date = patient.getDob().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            uq.update("PATIENTS")
                    .set("name", "N'" + patient.getName() + "'")
                    .set("date_of_birth", "'" + date + "'")
                    .set("address_province_code", "N'" + patient.getAddress().getProvince().getCode() + "'")
                    .set("address_district_code", "N'" + patient.getAddress().getDistrict().getCode() + "'")
                    .set("address_ward_code", "N'" + patient.getAddress().getWard().getCode() + "'")
                    .set("address_line", "N'" + patient.getAddress().getAddressLine() + "'")
                    .where("username", "'" + patient.getUsername() + "'");

            try {
                DatabaseCommunication.getInstance().execute(uq.getQuery());
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    //! 1.2.4 product manager
    public ArrayList<Product> findProductByName(String name) {
        ArrayList<Product> found = new ArrayList<>();
        SelectQuery selectQuery = new SelectQuery();
        selectQuery.from("PRODUCTS").select("*").where("name=N'" + name + "'");
        try {
            List<Map<String, Object>> rs = DatabaseCommunication.getInstance().executeQuery(selectQuery.getQuery());
            for (Map<String, Object> map : rs) {
                Product product = new Product(
                        String.valueOf(map.get("product_id")),
                        String.valueOf(map.get("name")),
                        String.valueOf(map.get("img_src")),
                        String.valueOf(map.get("unit")),
                        (Double) map.get("price")
                );
                found.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Error finding product: " + e.getMessage());
        } finally {
            return found;
        }
    }

    public HashMap<String, Product> filterProduct(/*filter*/) {
        if (getRole() == Role.MANAGER) {
            return App.getInstance().getProductManagement().filterProduct(/*filter*/);
        }
        return null;
    }

    public boolean addProduct(Product product) {
        if (getRole() == Role.MANAGER) {
            ArrayList<String> columns = new ArrayList<>() {
                {
                    add("product_id");
                    add("name");
                    add("img_src");
                    add("unit");
                    add("price");
                }
            };
            ArrayList<String> values = new ArrayList<>() {
                {
                    add("'" + product.getId() + "'");
                    add("N'" + product.getName() + "'");
                    add("N'" + product.getImgSrc() + "'");
                    add("N'" + product.getUnit() + "'");
                    add(String.valueOf(product.getPrice()));
                }
            };
            InsertQuery insertQuery = new InsertQuery();
            insertQuery.insertInto("PRODUCTS").columns(columns).values(values);
            try {
                DatabaseCommunication.getInstance().execute(insertQuery.getQuery());
            } catch (SQLException e) {
                System.out.println("Exception adding new product: " + e.getMessage());
                return false;
            } finally {
                return true;
            }
        }
        return false;
    }

    public boolean deleteProduct(String id) {
        if (getRole() == Role.MANAGER) {
            DeleteQuery deleteQuery = new DeleteQuery();
            deleteQuery.deleteFrom("PRODUCTS").where("product_id='" + id + "'");
            try {
                DatabaseCommunication.getInstance().execute(deleteQuery.getQuery());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            } finally {
                return true;
            }
        }
        return false;
    }

    public boolean updateProduct(String productId, Product newProductInfo) {
        if (getRole() == Role.MANAGER) {
            UpdateQuery updateQuery = new UpdateQuery();
            updateQuery.update("PRODUCTS")
                    .where("product_id='" + productId + "'")
                    .set("name", "N'" + newProductInfo.getName() + "'")
                    .set("img_src", "'" + newProductInfo.getImgSrc() + "'")
                    .set("unit", "'" + newProductInfo.getUnit() + "'")
                    .set("price", String.valueOf(newProductInfo.getPrice()));
            try {
                DatabaseCommunication.getInstance().execute(updateQuery.getQuery());
            } catch (Exception e) {
                return false;
            } finally {
                return true;
            }
        }
        return false;
    }

    //! 1.2.5 package management
    public void showPackageList() {
        if (getRole() == Role.MANAGER) {
            SelectQuery selectQuery = new SelectQuery();
            selectQuery.select("*").from("PACKAGES");
            try {
                List<Map<String, Object>> rs = DatabaseCommunication.getInstance().executeQuery(selectQuery.getQuery());
                DatabaseCommunication.getInstance().printResult(rs);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public HashMap<String, Package> getPackages() {
        DatabaseCommunication.getInstance().loadProductsAndPackages();
        return App.getInstance().getProductManagement().getPackageList();
    }

    public HashMap<String, Product> getProducts() {
        DatabaseCommunication.getInstance().loadProductsAndPackages();
        return App.getInstance().getProductManagement().getProductList();
    }

    public HashMap<String, Product> getSortedProducts() {
        try {
            DatabaseCommunication dbc = DatabaseCommunication.getInstance();
            SelectQuery selectProducts = new SelectQuery();
            selectProducts
                    .select("*")
                    .from("PRODUCTS")
                    .orderBy("price");

            List<Map<String, Object>> rs = dbc.executeQuery(selectProducts.getQuery());
            App.getInstance().getProductManagement().getProductList().clear();
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return App.getInstance().getProductManagement().getProductList();
    }

    public HashMap<String, Patient> getPatients() {
        HashMap<String, Patient> patientList = new HashMap<>();
        try {
            DatabaseCommunication dbc = DatabaseCommunication.getInstance();
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

            List<Map<String, Object>> rs = dbc.executeQuery(selectPatients.getQuery());

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
                Patient p = new Patient(
                        new UserConcreteComponent(username, name, password, IUser.Role.PATIENT),
                        status,
                        dob.toLocalDate(),
                        new Location(address_line,
                                App.getInstance().getWardList().get(w_code),
                                App.getInstance().getDistrictList().get(d_code),
                                App.getInstance().getProvinceList().get(p_code)),
                        App.getInstance().getTreatmentLocationList().get(tloc_code),
                        new ArrayList<>()
                );
                patientList.put(username, p);


            });

            // load close contacts
            SelectQuery selectCloseContacts = new SelectQuery();
            selectCloseContacts
                    .select("*")
                    .from("CLOSE_CONTACTS");
            rs = dbc.executeQuery(selectCloseContacts.getQuery());

            rs.forEach(map -> {
                String f_upper_username = String.valueOf(map.get("f_upper_username")),
                        f_lower_username = (String) map.get("f_lower_username");

                patientList
                        .get(f_upper_username)
                        .addCloseContact((Patient) App
                                .getInstance()
                                .getUserList()
                                .get(f_lower_username));
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patientList;
    }

    public ArrayList<Package> findPackageByName(String name) {
        if (getRole() == Role.MANAGER) {
            ArrayList<Package> found = new ArrayList<>();
            SelectQuery selectQuery = new SelectQuery();
            selectQuery.select("*").from("PACKAGES").where("name=N'" + name + "'");
            try {
                // todo: review productList in constructor
                List<Map<String, Object>> rs = DatabaseCommunication.getInstance().executeQuery(selectQuery.getQuery());
                for (Map<String, Object> map : rs) {
                    Package newPackage = new Package(
                            String.valueOf(map.get("package_id")),
                            String.valueOf(map.get("name")),
                            String.valueOf(map.get("img_src")),
                            (Integer) map.get("purchased_amount_limit"),
                            (Integer) map.get("time_limit"),
                            (Double) map.get("price"),
                            new HashMap<>()
                    );
                    found.add(newPackage);
                }
            } catch (SQLException e) {
                System.out.println("Error finding package: " + e.getMessage());
            } finally {
                return found;
            }
        }
        return null;
    }

    public void sortPackageList(ArrayList<String> orders) {
        SelectQuery selectQuery = new SelectQuery();
        selectQuery.from("PACKAGES").select("*").orderBy(orders);
        try {
            List<Map<String, Object>> rs = DatabaseCommunication.getInstance().executeQuery(selectQuery.getQuery());
            DatabaseCommunication.getInstance().printResult(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public HashMap<String, Package> filterPackage(/*filter*/) {
        if (getRole() == Role.MANAGER) {
            return App.getInstance().getProductManagement().filterPackage(/*filter*/);
        }
        return null;
    }

    public void addProductToPackage(String product_id, String package_id, int quantity) {
        ArrayList<String> columns = new ArrayList<>() {
            {
                add("product_id");
                add("package_id");
                add("quantity");
            }
        };
        ArrayList<String> values = new ArrayList<>() {
            {
                add("'" + product_id + "'");
                add("'" + package_id + "'");
                add(String.valueOf(quantity));
            }
        };
        InsertQuery insertQuery = new InsertQuery();
        insertQuery.insertInto("PRODUCTS_IN_PACKAGES").columns(columns).values(values);
        try {
            DatabaseCommunication.getInstance().execute(insertQuery.getQuery());
        } catch (SQLException e) {
            System.out.println("Exception adding new record to PRODUCTS_IN_PACKAGES: " + e.getMessage());
        }
    }

    public boolean addPackage(Package pkg) {
        if (getRole() == Role.MANAGER) {
            ArrayList<String> columns = new ArrayList<>() {
                {
                    add("package_id");
                    add("name");
                    add("img_src");
                    add("purchased_amount_limit");
                    add("time_limit");
                    add("price");
                }
            };
            ArrayList<String> values = new ArrayList<>() {
                {
                    add("'" + pkg.getId() + "'");
                    add("N'" + pkg.getName() + "'");
                    add("'" + pkg.getImg_src() + "'");
                    add(String.valueOf(pkg.getPurchasedAmountLimit()));
                    add(String.valueOf(pkg.getTimeLimit()));
                    add(String.valueOf(pkg.getPrice()));
                }
            };

            InsertQuery insertQuery = new InsertQuery();
            insertQuery.insertInto("PACKAGES").columns(columns).values(values);
            try {
                // todo: data integrity
                DatabaseCommunication.getInstance().execute(insertQuery.getQuery());
                HashMap<String, Pair<Product, Integer>> map = pkg.getProductList();
                map.forEach((key, value) -> {
                    /*
                     * key: product id
                     * value: Pair<Product prod, Integer quantity>
                     * */
                    addProductToPackage(key, pkg.getId(), value.getSecond());
                });
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            } finally {
                //update in class App
                return true;
            }
        }
        return false;
    }

    public boolean updatePackage(String id, Package newPackageInfo) {
        // todo: write better exc handling
        if (getRole() == Role.MANAGER) {
            UpdateQuery updateQuery = new UpdateQuery();
            updateQuery.update("PRODUCTS")
                    .where("product_id='" + id + "'")
                    .set("name", "N'" + newPackageInfo.getName() + "'")
                    .set("img_src", "'" + newPackageInfo.getImg_src() + "'")
                    .set("purchased_amount_limit", String.valueOf(newPackageInfo.getPurchasedAmountLimit()))
                    .set("time_limit", String.valueOf(newPackageInfo.getTimeLimit()))
                    .set("price", String.valueOf(newPackageInfo.getPrice()));
            try {
                DatabaseCommunication.getInstance().execute(updateQuery.getQuery());
                HashMap<String, Pair<Product, Integer>> map = newPackageInfo.getProductList();
                map.forEach((key, value) -> {
                    /*
                     * key: product id
                     * value: Pair<Product prod, Integer quantity>
                     * */
                    addProductToPackage(key, newPackageInfo.getId(), value.getSecond());
                });
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            } finally {
                //return App.getInstance().getProductManagement().updatePackage(id, newPackageInfo);
            }
        }
        return false;
    }

    public boolean deletePackage(String id) {
        if (getRole() == Role.MANAGER) {
            DeleteQuery deletePackage = new DeleteQuery();
            DeleteQuery deleteProductPackage = new DeleteQuery();
            deleteProductPackage.deleteFrom("PRODUCTS_IN_PACKAGES")
                    .where("package_id='" + id + "'");
            deletePackage.deleteFrom("PACKAGES")
                    .where("package_id='" + id + "'");
            try {
                DatabaseCommunication.getInstance().execute(deleteProductPackage.getQuery());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
            try {
                DatabaseCommunication.getInstance().execute(deletePackage.getQuery());
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return false;
            }
            return true;
        }
        return false;
    }

    //! 1.2.6
    public void showStatic(String option) {
        if (getRole() == Role.MANAGER) {
            switch (option) {
                // show UI or Static here
                case "default":
                    return;
            }
        }
    }
}