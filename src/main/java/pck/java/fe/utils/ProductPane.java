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
//        pane.setMinSize(195, 250);
        pane.setPrefSize(256, 370);
//        pane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        //img view
        ImageView productImg = new ImageView(new Image(product.getImgSrc()));
        productImg.setFitWidth(230);
        productImg.setFitHeight(230);
        productImg.setLayoutX(15);
        pane.getChildren().add(productImg);

        // label name
        Label productName = new Label(product.getName());
        productName.setTextAlignment(TextAlignment.CENTER);
        productName.setStyle("-fx-font-family: Arial;-fx-font-size:16; -fx-text-fill: #132ac1;");
        productName.setPrefSize(200, 20);
        productName.setLayoutY(230);
        productName.setAlignment(Pos.CENTER);
        pane.getChildren().add(productName);

        // label price
        Label productPrice = new Label(String.valueOf(product.getPrice()) + " VNĐ");
        productPrice.setTextAlignment(TextAlignment.CENTER);
        productPrice.setAlignment(Pos.CENTER);
        productPrice.setStyle("-fx-font-family: Arial;");
        productPrice.setPrefSize(200, 20);
        productPrice.setLayoutY(250);
        pane.getChildren().add(productPrice);

        // pane quantity = pq
        Pane paneQuantity = new Pane();
        paneQuantity.setPrefSize(200, 30);
        paneQuantity.setLayoutY(270);
        pane.getChildren().add(paneQuantity);

        // pq > text quantity = tq
        TextField tq = new TextField();
        tq.setPrefSize(50, 25);
        tq.setAlignment(Pos.CENTER);
        tq.setLayoutX(75);
        tq.setLayoutY(2);
        tq.setText("0");
        paneQuantity.getChildren().add(tq);
        tq.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    tq.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        // pq > iMinus
        ImageView iMinus = new ImageView(new Image(iconMinusURL));
        iMinus.setFitWidth(20);
        iMinus.setFitHeight(20);
        iMinus.setLayoutX(40);
        iMinus.setLayoutY(5);
        paneQuantity.getChildren().add(iMinus);

        iMinus.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println(getClass() + "iMinus clicked");
                // decrease quantity by 1
                String preQuantityStr = tq.getText();
                try {
                    int quantity = Integer.parseInt(preQuantityStr);
                    quantity -= 1;

                    if (quantity >= 0) {
                        tq.setText(String.valueOf(quantity));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    tq.setText("0");
                }
            }
        });

        // pq > iAdd
        ImageView iAdd = new ImageView(new Image(iconPlusURL));
        iAdd.setFitWidth(20);
        iAdd.setFitHeight(20);
        iAdd.setLayoutX(140);
        iAdd.setLayoutY(5);
        paneQuantity.getChildren().add(iAdd);

        iAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println(getClass() + "iAdd clicked");
                // increase quantity by 1
                String preQuantityStr = tq.getText();
                try {
                    int quantity = Integer.parseInt(preQuantityStr);
                    quantity += 1;

                    tq.setText(String.valueOf(quantity));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    tq.setText("0");
                }
            }
        });

        // pane > btnAddProd
        Button btnAddProd = new Button("Thêm vào giỏ hàng");
        btnAddProd.setAlignment(Pos.CENTER);
        btnAddProd.setPrefSize(120, 25);
        btnAddProd.setLayoutX(40);
        btnAddProd.setLayoutY(300);
        pane.getChildren().add(btnAddProd);

        btnAddProd.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println(getClass()+ " add prod to cart");
                // todo: add product to cart
            }
        });
    }

    public Pane getPane() {
        return pane;
    }
}
