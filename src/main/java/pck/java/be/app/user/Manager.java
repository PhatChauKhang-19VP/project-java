package pck.java.be.app.user;

import pck.java.be.app.App;
import pck.java.be.app.database.*;
import pck.java.be.app.product.Package;
import pck.java.be.app.product.Product;
import pck.java.be.app.util.Pair;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // todo: change treatment_loc in patient to string or hashmap
//    public Patient findPatientWithId(String username) {
//        SelectQuery selectQuery = new SelectQuery();
//        selectQuery.select("*").from("PATIENTS").where("username='"+ username + "'");
//        try {
//            List<Map<String, Object>> rs = DatabaseCommunication.getInstance().executeQuery(selectQuery.getQuery());
//            rs.forEach(map -> {
//                Patient p = new Patient(
//                        new UserConcreteComponent(
//                                username,
//                                String.valueOf(map.get("name")),
//                                "",
//                                Role.PATIENT),
//                        (Integer) map.get("f_status"),
//                        LocalDate.parse(String.valueOf(map.get("dob"))),
//
//                )
//            });
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

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
                    add("'" + user.getPassword() + "'");
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
                }
            };
            ArrayList<String> values = new ArrayList<>() {
                {
                    add("'" + newPatient.getUsername() + "'");
                    add("'" + newPatient.getName() + "'");
                    add(String.valueOf(newPatient.getStatus()));
                    add("'" + java.sql.Date.valueOf(newPatient.getDob()) + "'");
                    add("'" + newPatient.getAddress().getProvince().getCode() + "'");
                    add("'" + newPatient.getAddress().getDistrict().getCode() + "'");
                    add("'" + newPatient.getAddress().getWard().getCode() + "'");
                    add("'" + newPatient.getAddress().getAddressLine() + "'");
                    add("'" + newPatient.getTreatmentLocation().getCode() + "'");
                }
            };
            insertQuery.insertInto("PATIENTS").columns(columns).values(values);
            try {
                DatabaseCommunication.getInstance().execute(insertQuery.getQuery());
            } catch (SQLException e) {
                e.printStackTrace();
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

    public void sortProductList(ArrayList<String> orders) {
        SelectQuery selectQuery = new SelectQuery();
        selectQuery.from("PRODUCTS").select("*").orderBy(orders);
        try {
            List<Map<String, Object>> rs = DatabaseCommunication.getInstance().executeQuery(selectQuery.getQuery());
            DatabaseCommunication.getInstance().printResult(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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