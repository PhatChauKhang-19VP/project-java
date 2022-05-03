package pck.java.be.app;

import pck.java.be.app.product.ProductManagement;
import pck.java.be.app.user.IUser;
import pck.java.be.app.util.District;
import pck.java.be.app.util.Province;
import pck.java.be.app.util.TreatmentLocation;
import pck.java.be.app.util.Ward;
import pck.java.database.DatabaseCommunication;
import pck.java.database.SelectQuery;

import javax.crypto.interfaces.PBEKey;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * App controller, implemented according to Singleton pattern.
 */
public class App {
    private static App instance = null;
    private IUser currentUser = null;
    private HashMap<String, Province> provinceList;
    private HashMap<String, District> districtList;
    private HashMap<String, Ward> wardList;
    private HashMap<String, IUser> userList;
    private HashMap<String, TreatmentLocation> treatmentLocationList;
    private ProductManagement productManagement;

    private App() {
        userList = new HashMap<>();
        provinceList = new HashMap<>();
        districtList = new HashMap<>();
        wardList = new HashMap<>();
        treatmentLocationList = new HashMap<>();
        productManagement = new ProductManagement();
    }

    public static App getInstance() {
        if (instance == null)
            instance = new App();
        return instance;
    }

    public static void main(String[] args) {
    }

    public void setUserList(HashMap<String, IUser> userList) {
        this.userList = userList;
    }

    public HashMap<String, IUser> getUserList() {
        return userList;
    }

    public void addUser(IUser newUser) {
        userList.put(newUser.getUsername(), newUser);
    }

    public boolean deleteUser(IUser user) {
        if (user.getRole() == IUser.Role.ADMIN) {
            return false;
        } else {
            userList.remove(user);
            return true;
        }
    }

    public void setTreatmentLocations(HashMap<String, TreatmentLocation> treatmentLocationList) {
        this.treatmentLocationList = treatmentLocationList;
    }

    public HashMap<String, TreatmentLocation> getTreatmentLocations() {
        return treatmentLocationList;
    }

    public HashMap<String, Province> getProvinceList() {
        DatabaseCommunication dbc = DatabaseCommunication.getInstance();
        SelectQuery sq = new SelectQuery();
        sq.select("*").from("PROVINCES");
        try {
            List<Map<String, Object>> rs = dbc.executeQuery(sq.getQuery());
            rs.forEach(map -> {
                String code = String.valueOf(map.get("code")),
                        name = String.valueOf(map.get("name")),
                        name_en = String.valueOf(map.get("name_en"));
                provinceList.put(code, new Province(code, name, name_en));
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return provinceList;
    }

    public void setProvinceList(HashMap<String, Province> provinceList) {
        this.provinceList = provinceList;
    }

    public HashMap<String, District> getDistrictList() {
        DatabaseCommunication dbc = DatabaseCommunication.getInstance();
        SelectQuery sq = new SelectQuery();
        sq.select("*").from("DISTRICTS");
        try {
            List<Map<String, Object>> rs = dbc.executeQuery(sq.getQuery());
            rs.forEach(map -> {
                String code = String.valueOf(map.get("code")),
                        name = String.valueOf(map.get("name")),
                        name_en = String.valueOf(map.get("name_en")),
                        belong_to = String.valueOf(map.get("province_code"));
                districtList.put(code, new District(code, name, name_en, provinceList.get(belong_to)));
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return districtList;
    }

    public void setDistrictList(HashMap<String, District> districtList) {
        this.districtList = districtList;
    }

    public HashMap<String, Ward> getWardList() {
        DatabaseCommunication dbc = DatabaseCommunication.getInstance();
        SelectQuery sq = new SelectQuery();
        sq.select("*").from("WARDS");
        try {
            List<Map<String, Object>> rs = dbc.executeQuery(sq.getQuery());
            rs.forEach(map -> {
                String code = String.valueOf(map.get("code")),
                        name = String.valueOf(map.get("name")),
                        name_en = String.valueOf(map.get("name_en")),
                        belong_to = String.valueOf(map.get("district_code"));
                wardList.put(code, new Ward(code, name, name_en, districtList.get(belong_to)));
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wardList;
    }

    public void setWardList(HashMap<String, Ward> wardList) {
        this.wardList = wardList;
    }

    public HashMap<String, TreatmentLocation> getTreatmentLocationList() {
        DatabaseCommunication dbc = DatabaseCommunication.getInstance();
        SelectQuery sq = new SelectQuery();
        sq.select("*").from("TREATMENT_LOCATIONS");
        try {
            List<Map<String, Object>> rs = dbc.executeQuery(sq.getQuery());
            rs.forEach(map -> {
                String code = String.valueOf(map.get("treatment_location_code")),
                        name = String.valueOf(map.get("name"));
                Integer capacity = Integer.valueOf(String.valueOf(map.get("capacity"))),
                        current_room = Integer.valueOf(String.valueOf(map.get("current_room")));
                treatmentLocationList.put(code, new TreatmentLocation(code, name, capacity, current_room));
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return treatmentLocationList;
    }

    public void setTreatmentLocationList(HashMap<String, TreatmentLocation> treatmentLocationList) {
        this.treatmentLocationList = treatmentLocationList;
    }

    public void setProductManagement(ProductManagement productManagement) {
        this.productManagement = productManagement;
    }

    public void addTreatmentLocation(TreatmentLocation newLocation) {
        this.treatmentLocationList.put(newLocation.getCode(), newLocation);
    }

    public boolean deleteTreatmentLocation(String code) {
        try {
            treatmentLocationList.remove(code);
        } catch (Exception e) {
            System.out.println("Exception deleting treatment location: " + e.getMessage());
        } finally {
            return true;
        }
    }

    public void showTreatmentLocationList() {
        for (String key : treatmentLocationList.keySet()) {
            System.out.println(treatmentLocationList.get(key));
        }
    }

    public ProductManagement getProductManagement() {
        return productManagement;
    }

    public IUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(IUser currentUser) {
        this.currentUser = currentUser;
    }
}