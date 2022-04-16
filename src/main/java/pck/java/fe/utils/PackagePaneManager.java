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

public class PackagePaneManager {
    private static final String iconMinusURL = "https://res.cloudinary.com/phatchaukhang/image/upload/v1649646897/HQTCSDL/Icon/minus-button_ukufql.png";
    private static final String iconPlusURL = "https://res.cloudinary.com/phatchaukhang/image/upload/v1649646899/HQTCSDL/Icon/plus_arj0go.png";
    Pane pane;
    Package pkg;

    public PackagePaneManager(Package pkg) {
        this.pkg = pkg;

        pane = new Pane();
        //pane.setBorder(ne);
        pane.setMinSize(242, 300);
        pane.setPrefSize(242, 300);

//        pane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        //img view
        ImageView packageImg = new ImageView(new Image(pkg.getImg_src()));
        packageImg.setFitWidth(242);
        packageImg.setFitHeight(242);
        packageImg.setLayoutX(0);
        packageImg.setLayoutY(-10);
        pane.getChildren().add(packageImg);

        // label name
        Label packageName = new Label(pkg.getName());
        packageName.setAlignment(Pos.CENTER);
        packageName.setTextAlignment(TextAlignment.CENTER);
        packageName.setStyle("-fx-font-family: Arial;-fx-font-size:16; -fx-text-fill: #132ac1;");
        packageName.setPrefSize(200, 20);
        packageName.setLayoutX(20);
        packageName.setLayoutY(200);
        packageName.setAlignment(Pos.CENTER);
        pane.getChildren().add(packageName);

        // label price
        Label packagePrice = new Label(String.valueOf(pkg.getPrice()) + " VNĐ");
        packagePrice.setAlignment(Pos.CENTER);
        packagePrice.setTextAlignment(TextAlignment.CENTER);
        packagePrice.setAlignment(Pos.CENTER);
        packagePrice.setStyle("-fx-font-family: Arial;");
        packagePrice.setPrefSize(200, 20);
        packagePrice.setLayoutX(20);
        packagePrice.setLayoutY(230);
        pane.getChildren().add(packagePrice);

        // pane > btnAddProd
        Button btnAddProd = new Button("Xem chi tiết");
        btnAddProd.setAlignment(Pos.CENTER);
        btnAddProd.setPrefSize(120, 25);
        btnAddProd.setLayoutX(60);
        btnAddProd.setLayoutY(260);
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
