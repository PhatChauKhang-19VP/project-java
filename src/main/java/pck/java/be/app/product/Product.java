package pck.java.be.app.product;

public class Product {
    private String id, name, imgSrc, unit;
    private double price;

    public Product() {
        id = name = imgSrc = unit = null;
    }

    public Product(String id, String name, String imgSrc, String unit, double price) {
        this.id = id;
        this.name = name;
        this.imgSrc = imgSrc;
        this.unit = unit;
        this.price = price;
    }

    public void update(Product product) {
        this.name = product.name;
        this.imgSrc = product.imgSrc;
        this.unit = product.unit;
        this.price = product.price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public String getUnit() {
        return unit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "\tProduct" +
                "\n\t\tid=" + id +
                "\n\t\tname=" + name +
                "\n\t\timgSrc=" + imgSrc +
                "\n\t\tunit=" + unit +
                "\n\t\tprice=" + price;
    }
}