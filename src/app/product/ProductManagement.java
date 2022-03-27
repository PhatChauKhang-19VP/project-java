package app.product;

import java.util.Comparator;
import java.util.HashMap;

public class ProductManagement {
    private HashMap<String, Product> productList;
    private HashMap<String, Package> packageList;
    private HashMap<Integer, Order> orderList;

    public ProductManagement() {
        productList = new HashMap<>();
        packageList = new HashMap<>();
    }

    public HashMap<String, Product> getProductList() {
        return productList;
    }

    public void setProductList(HashMap<String, Product> productList) {
        this.productList = productList;
    }

    public HashMap<String, Package> getPackageList() {
        return packageList;
    }

    public void setPackageList(HashMap<String, Package> packageList) {
        this.packageList = packageList;
    }

    public HashMap<Integer, Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(HashMap<Integer, Order> orderList) {
        this.orderList = orderList;
    }

    public Product findProductById(String id) {
        return productList.get(id);
    }

    public Product findProductByName(String name) {
        for (String key : productList.keySet()) {
            if (productList.get(key).getName().equals(name)) {
                return productList.get(key);
            }
        }
        return null;
    }

    public Package findPackageByName(String name) {
        for (String key : packageList.keySet()) {
            if (packageList.get(key).getName().equals(name)) {
                return packageList.get(key);
            }
        }

        return null;
    }

    public Package findPackageById(String id) {
        return packageList.get(id);
    }

    public void sortProductList(Comparator comparator) {
        //sort
        System.out.println("Product list is sorted");
    }

    public void sortPackageList(Comparator comparator) {
        //sort
        System.out.println("Package list is sorted");
    }

    public HashMap<String, Product> filterProduct(/*filter params*/) {
        return productList;
    }

    public HashMap<String, Package> filterPackage(/*filter params*/) {
        return packageList;
    }

    public void addProduct(Product product) {
        productList.put(product.getId(), product);
    }

    public void addPackage(Package pkg) {
        packageList.put(pkg.getId(), pkg);
    }

    public boolean updateProduct(String id, Product newProductInfo) {
        Product product = findProductById(id);
        if (product != null) {
            product.update(newProductInfo);
            return true;
        }
        return false;
    }

    public boolean updatePackage(String id, Package newPackageInfo) {
        Package pkg = findPackageById(id);
        if (pkg != null) {
            pkg.update(newPackageInfo);
            return true;
        }
        return false;
    }

    public boolean deleteProduct(String id) {
        if (productList.containsKey(id)){
            productList.remove(id);
            return true;
        }
        return false;
    }

    public boolean deletePackage(String id) {
        if (packageList.containsKey(id)){
            packageList.remove(id);

            return  true;
        }
        return  false;
    }

    public void addProductToPackage(String package_id, Product product, int quantity){
        packageList.get(package_id).addProduct(product, quantity);
    }

    public void showInfo(){
        for (String key : packageList.keySet()){
            System.out.println(packageList.get(key));
            System.out.println("\n=========================================================\n");
        }
    }
}