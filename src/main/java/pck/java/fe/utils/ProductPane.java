package pck.java.fe.utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import pck.java.Index;
import pck.java.be.app.product.Package;
import pck.java.be.app.product.Product;

import java.io.IOException;

public class ProductPane {
    private static final String iconMinusURL = "https://res.cloudinary.com/phatchaukhang/image/upload/v1649646897/HQTCSDL/Icon/minus-button_ukufql.png";
    private static final String iconPlusURL = "https://res.cloudinary.com/phatchaukhang/image/upload/v1649646899/HQTCSDL/Icon/plus_arj0go.png";
    Pane pane;
    Product product;

    public ProductPane(Product product) {
        this.product = product;

        pane = new Pane();
        //pane.setBorder(ne);
        pane.setMinSize(242, 300);
        pane.setPrefSize(242, 300);
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
        Label productPrice = new Label(String.valueOf(product.getPrice()) + " VNĐ");
        productPrice.setTextAlignment(TextAlignment.CENTER);
        productPrice.setAlignment(Pos.CENTER);
        productPrice.setStyle("-fx-font-family: Arial;");
        productPrice.setPrefSize(200, 20);
        productPrice.setLayoutX(20);
        productPrice.setLayoutY(200);
        pane.getChildren().add(productPrice);

        // pane > btnModProd
        Button btnModProd = new Button("Chỉnh sửa");
        btnModProd.setAlignment(Pos.CENTER);
        btnModProd.setPrefSize(120, 25);
        btnModProd.setLayoutX(60);
        btnModProd.setLayoutY(230);
        pane.getChildren().add(btnModProd);

        btnModProd.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println(getClass() + "btn mod prod clicked");
                try {
                    Stage modalModProd = new Stage();
                    FXMLLoader loader = new FXMLLoader(Index.class.getResource("manager.modalMngrModProd.fxml"), null, new JavaFXBuilderFactory());
                    Parent root = loader.load();
                    modalModProd.initOwner(Index.getInstance().getStage());
                    modalModProd.setScene(new Scene(root));
                    modalModProd.setTitle("Sửa thông tin nhu yếu phẩm");
                    modalModProd.initModality(Modality.APPLICATION_MODAL);

                    modalModProd.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
                    modalModProd.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                    modalModProd.setResizable(false);
                    modalModProd.setFullScreen(false);
                    modalModProd.sizeToScene();

                    modalModProd.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // pane > btnModProd
        Button btnDelProd = new Button("Xóa sản phẩm");
        btnDelProd.setAlignment(Pos.CENTER);
        btnDelProd.setPrefSize(120, 25);
        btnDelProd.setLayoutX(60);
        btnDelProd.setLayoutY(260);
        btnDelProd.getStyleClass().addAll("btn", "btn-danger");
        pane.getChildren().add(btnDelProd);
    }

    public Pane getPane() {
        return pane;
    }
}
