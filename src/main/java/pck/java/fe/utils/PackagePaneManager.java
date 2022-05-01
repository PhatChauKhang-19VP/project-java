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
        pane.setMinSize(252, 320);
        pane.setPrefSize(252, 320);

//        pane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        //img view
        ImageView packageImg = new ImageView(new Image(pkg.getImg_src()));
        packageImg.setFitWidth(242);
        packageImg.setFitHeight(242);
        packageImg.setLayoutX(5);
        packageImg.setLayoutY(-10);
        pane.getChildren().add(packageImg);

        // label name
        Label packageName = new Label(pkg.getName());
        packageName.setAlignment(Pos.CENTER);
        packageName.setTextAlignment(TextAlignment.CENTER);
        packageName.setStyle("-fx-font-family: Arial;-fx-font-size:16; -fx-text-fill: #132ac1;");
        packageName.setPrefSize(200, 20);
        packageName.setLayoutX(26);
        packageName.setLayoutY(220);
        packageName.setAlignment(Pos.CENTER);
        pane.getChildren().add(packageName);

        // label price
        Label packagePrice = new Label(String.format("%.3f VNĐ", pkg.getPrice()));
        packagePrice.setAlignment(Pos.CENTER);
        packagePrice.setTextAlignment(TextAlignment.CENTER);
        packagePrice.setAlignment(Pos.CENTER);
        packagePrice.setStyle("-fx-font-family: Arial;");
        packagePrice.setPrefSize(200, 20);
        packagePrice.setLayoutX(26);
        packagePrice.setLayoutY(250);
        pane.getChildren().add(packagePrice);

        // pane > btnAddProd
        Button btnDetailProd = new Button("Xem chi tiết");
        btnDetailProd.setAlignment(Pos.CENTER);
        btnDetailProd.setPrefSize(120, 25);
        btnDetailProd.setLayoutX(66);
        btnDetailProd.setLayoutY(280);
        pane.getChildren().add(btnDetailProd);

        btnDetailProd.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println(getClass() + " pakages detail");
                // todo: show package detail
            }
        });
    }

    public Pane getPane() {
        return pane;
    }
}
