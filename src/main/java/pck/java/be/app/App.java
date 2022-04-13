package pck.java.be.app;

import pck.java.be.app.product.ProductManagement;
import pck.java.be.app.user.IUser;
import pck.java.be.app.util.District;
import pck.java.be.app.util.Province;
import pck.java.be.app.util.TreatmentLocation;
import pck.java.be.app.util.Ward;

import java.util.HashMap;

/**
 * App controller, implemented according to Singleton pattern.
 */
public class App {
    private static App instance = null;
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
        return provinceList;
    }

    public void setProvinceList(HashMap<String, Province> provinceList) {
        this.provinceList = provinceList;
    }

    public HashMap<String, District> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(HashMap<String, District> districtList) {
        this.districtList = districtList;
    }

    public HashMap<String, Ward> getWardList() {
        return wardList;
    }

    public void setWardList(HashMap<String, Ward> wardList) {
        this.wardList = wardList;
    }

    public HashMap<String, TreatmentLocation> getTreatmentLocationList() {
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
}