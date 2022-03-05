package app.product;

import java.util.ArrayList;
import java.util.Comparator;

public class ProductManagement {
    private ArrayList<Product> productList;
    private ArrayList<Package> packageList;

    public ProductManagement() {
        productList = new ArrayList<>();
        packageList = new ArrayList<>();
    }

    public ProductManagement(ArrayList<Product> productList, ArrayList<Package> packageList) {
        this.productList = productList;
        this.packageList = packageList;
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }

    public ArrayList<Package> getPackageList() {
        return packageList;
    }

    public void setPackageList(ArrayList<Package> packageList) {
        this.packageList = packageList;
    }

    public Product findProductById(String id) {
        for (Product product : productList) {
            if (product.getName().equals(id)) {
                return product;
            }
        }
        return null;
    }

    public Product findProductByName(String name) {
        for (Product product : productList) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    public Package findPackageByName(String name) {
        for (Package pkg : packageList) {
            if (pkg.getName().equals(name)) {
                return pkg;
            }
        }

        return null;
    }

    public Package findPackageById(String id) {
        for (Package pkg : packageList) {
            if (pkg.getName().equals(id)) {
                return pkg;
            }
        }

        return null;
    }

    public void sortProductList(Comparator comparator) {
        //sort
        System.out.println("Product list is sorted");
    }

    public void sortPackageList(Comparator comparator) {
        //sort
        System.out.println("Package list is sorted");
    }

    public ArrayList<Product> filterProduct(/*filter params*/) {
        return productList;
    }

    public ArrayList<Package> filterPackage(/*filter params*/) {
        return packageList;
    }

    public void addProduct(Product product) {
        productList.add(product);
    }

    public void addPackage(Package pkg) {
        packageList.add(pkg);
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
        Product product = findProductById(id);
        return productList.remove(product);
    }

    public boolean deletePackage(String id) {
        Package pkg = findPackageById(id);
        return packageList.remove(pkg);
    }
}