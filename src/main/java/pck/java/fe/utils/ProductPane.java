package pck.java.fe.utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import pck.java.be.app.product.Package;
import pck.java.be.app.product.Product;

public class ProductPane {
    private static final String iconMinusURL = "https://res.cloudinary.com/phatchaukhang/image/upload/v1649646897/HQTCSDL/Icon/minus-button_ukufql.png";
    private static final String iconPlusURL = "https://res.cloudinary.com/phatchaukhang/image/upload/v1649646899/HQTCSDL/Icon/plus_arj0go.png";
    Pane pane;
    Product product;

    public ProductPane(Product product) {
        this.product = product;

        pane = new Pane();
        //pane.setBorder(ne);
        pane.setMinSize(242, 280);
        pane.setPrefSize(242, 280);
//        pane.setStyle("-fx-background-color: #FFFFFF");
//        pane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        //img view
        ImageView productImg = new ImageView(new Image(product.getImgSrc()));
        productImg.setFitWidth(180);
        productImg.setFitHeight(150);
        productImg.setLayoutX(30);
        productImg.setLayoutY(20);
        pane.getChildren().add(productImg);

        // label name
        Label productName = new Label(product.getName());
        productName.setTextAlignment(TextAlignment.CENTER);
        productName.setStyle("-fx-font-family: Arial;-fx-font-size:16; -fx-text-fill: #132ac1;");
        productName.setPrefSize(200, 20);
        productName.setLayoutX(20);
        productName.setLayoutY(180);
        productName.setAlignment(Pos.CENTER);
        pane.getChildren().add(productName);

        // label price
        Label productPrice = new Label(String.format("%.3f VNĐ", product.getPrice()));
        productPrice.setTextAlignment(TextAlignment.CENTER);
        productPrice.setAlignment(Pos.CENTER);
        productPrice.setStyle("-fx-font-family: Arial;");
        productPrice.setPrefSize(200, 20);
        productPrice.setLayoutX(20);
        productPrice.setLayoutY(200);
        pane.getChildren().add(productPrice);

        // pane > btnAddProd
        Button btnAddProd = new Button("Chỉnh sửa");
        btnAddProd.setAlignment(Pos.CENTER);
        btnAddProd.setPrefSize(120, 25);
        btnAddProd.setLayoutX(60);
        btnAddProd.setLayoutY(230);
        pane.getChildren().add(btnAddProd);

        btnAddProd.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println(getClass() + " add prod to cart");
                // todo: add product to cart
            }
        });
    }

    public Pane getPane() {
        return pane;
    }
}
