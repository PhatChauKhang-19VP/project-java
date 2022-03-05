package app.product;

import java.time.Duration;
import java.util.HashMap;

public class Package {
    private String id;
    private String name;
    private int purchasedAmountLimit;
    private Duration timeLimit;
    private double price = 0.0D;
    private HashMap<Product, Integer> productList;

    Package() {
        productList = new HashMap<Product, Integer>();
    }

    public Package(String name, HashMap<Product, Integer> productList, int purchasedAmountLimit, Duration timeLimit) {
        this.name = name;
        this.productList = productList;
        this.purchasedAmountLimit = purchasedAmountLimit;
        this.timeLimit = timeLimit;
        productList.forEach((product, quantity) -> {
            price += product.getPrice() * quantity;
        });
    }

    public Package(HashMap<Product, Integer> productList) {
        this.productList = productList;
        productList.forEach((product, quantity) -> {
            price += product.getPrice() * quantity;
        });
    }

    void addProduct(Product product, int quantity) {
        productList.put(product, quantity);
        price += product.getPrice() * quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPurchasedAmountLimit() {
        return purchasedAmountLimit;
    }

    public void setPurchasedAmountLimit(int purchasedAmountLimit) {
        this.purchasedAmountLimit = purchasedAmountLimit;
    }

    public Duration getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Duration timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public HashMap<Product, Integer> getProductList() {
        return productList;
    }

    public void setProductList(HashMap<Product, Integer> productList) {
        this.productList = productList;
        productList.forEach((product, quantity) -> {
            price += product.getPrice() * quantity;
        });
    }

    //add remove product
    public void update(Package pkg) {
        // update pkg
    }

}