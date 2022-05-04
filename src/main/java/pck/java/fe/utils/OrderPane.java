package pck.java.fe.utils;

import javafx.scene.layout.Pane;
import pck.java.be.app.product.Product;

public class OrderPane {
    Pane pane;
    Product product;

    public OrderPane() {
        pane = new Pane();
        pane.setMinSize(252, 300);
        pane.setPrefSize(252, 300);
    }
}
