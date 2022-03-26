package app.product;

import app.util.Pair;

import java.util.HashMap;

public class Package {
    private String id, name, img_src;
    private int purchasedAmountLimit;
    private int timeLimit;
    private double price = 0.0D;
    private HashMap<String, Pair<Product, Integer>> productList;

    Package() {
        productList = new HashMap<>();
    }

    public Package(String id, String name, String img_src, int purchasedAmountLimit, int timeLimit, double price, HashMap<String, Pair<Product, Integer>> productList) {
        this.id = id;
        this.name = name;
        this.img_src = img_src;
        this.purchasedAmountLimit = purchasedAmountLimit;
        this.timeLimit = timeLimit;
        this.price = price;
        this.productList = productList;
    }

    public Package(HashMap<String, Pair<Product, Integer>> productList) {
        this.productList = productList;
        productList.forEach((id, pair) -> {
            price += pair.getFirst().getPrice()*pair.getSecond();
        });
    }

    void addProduct(Product product, int quantity) {
        productList.put(product.getId(), new Pair(product, quantity));
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

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
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

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }

    public HashMap<String, Pair<Product, Integer>> getProductList() {
        return productList;
    }

    public void setProductList(HashMap<String, Pair<Product, Integer>> productList) {
        this.productList = productList;
    }

    //add remove product
    public void update(Package pkg) {
        // update pkg
    }

    @Override
    public String toString() {
        String ret = "";
        ret += "Package: " +
                " id=" + id +
                ", name=" + name +
                ", img_src=" + img_src +
                ", purchasedAmountLimit=" + purchasedAmountLimit +
                ", timeLimit=" + timeLimit +
                ", price=" + price +
                "\n";

        for (String key : productList.keySet()){
            ret += "\n" + productList.get(key).getFirst() + "\n\t\tquantity=" + productList.get(key).getSecond();
        }

        return ret;
    }
}