package pck.java.be.app.product;

import pck.java.be.app.user.IUser;
import pck.java.be.app.util.Pair;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Order {
    private String id;
    private IUser belong_to = null;
    private LocalDateTime at_datetime;

    private HashMap<String, Pair<Package, Integer>> packageList;
    private double total;

    public Order(String id, IUser belong_to, LocalDateTime at_datetime, HashMap<String, Pair<Package, Integer>> packageList, double total) {
        this.id = id;
        this.belong_to = belong_to;
        this.at_datetime = at_datetime;
        this.packageList = packageList;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IUser getBelong_to() {
        return belong_to;
    }

    public void setBelong_to(IUser belong_to) {
        this.belong_to = belong_to;
    }

    public LocalDateTime getAt_datetime() {
        return at_datetime;
    }

    public void setAt_datetime(LocalDateTime at_datetime) {
        this.at_datetime = at_datetime;
    }

    public HashMap<String, Pair<Package, Integer>> getPackageList() {
        return packageList;
    }

    public void setPackageList(HashMap<String, Pair<Package, Integer>> packageList) {
        this.packageList = packageList;
    }

    public void savePackageDetail(Package pkg, int quantity) {
        packageList.put(pkg.getId(), new Pair<>(pkg, quantity));
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Order" +
                "\n\tid=" + id +
                "\n\tbelong_to=" + belong_to.getUsername() +
                "\n\tat_datetime=" + at_datetime +
                "\n\ttotal=" + total + "\n";
    }
}
