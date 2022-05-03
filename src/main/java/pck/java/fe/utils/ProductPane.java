package pck.java.fe.utils;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import pck.java.Index;
import pck.java.be.app.product.Product;
import pck.java.database.DatabaseCommunication;
import pck.java.database.DeleteQuery;
import pck.java.fe.manager.modal.ModProdController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ProductPane {
    private static final String iconMinusURL = "https://res.cloudinary.com/phatchaukhang/image/upload/v1649646897/HQTCSDL/Icon/minus-button_ukufql.png";
    private static final String iconPlusURL = "https://res.cloudinary.com/phatchaukhang/image/upload/v1649646899/HQTCSDL/Icon/plus_arj0go.png";
    Pane pane;
    Product product;

    public ProductPane(Product product) {
        this.product = product;

        pane = new Pane();
        //pane.setBorder(ne);
        pane.setMinSize(252, 300);
        pane.setPrefSize(252, 300);
//        pane.setStyle("-fx-background-color: #FFFFFF");
//        pane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        //img view
        ImageView productImg = new ImageView(new Image(product.getImgSrc()));
        productImg.setFitWidth(180);
        productImg.setFitHeight(150);
        productImg.setLayoutX(36);
        productImg.setLayoutY(20);
        pane.getChildren().add(productImg);

        // label name
        Label productName = new Label(product.getName());
        productName.setTextAlignment(TextAlignment.CENTER);
        productName.setStyle("-fx-font-family: Arial;-fx-font-size:16; -fx-text-fill: #132ac1;");
        productName.setPrefSize(200, 20);
        productName.setLayoutX(26);
        productName.setLayoutY(180);
        productName.setAlignment(Pos.CENTER);
        pane.getChildren().add(productName);

        // label price
        Label productPrice = new Label(String.format("%.3f VNĐ", product.getPrice()));
        productPrice.setTextAlignment(TextAlignment.CENTER);
        productPrice.setAlignment(Pos.CENTER);
        productPrice.setStyle("-fx-font-family: Arial;");
        productPrice.setPrefSize(200, 20);
        productPrice.setLayoutX(26);
        productPrice.setLayoutY(200);
        pane.getChildren().add(productPrice);

        // pane > btnModProd
        Button btnModProd = new Button("Chỉnh sửa");
        btnModProd.setAlignment(Pos.CENTER);
        btnModProd.setPrefSize(120, 25);
        btnModProd.setLayoutX(66);
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
                    ModProdController ctrl = loader.getController();
                    ctrl.setProduct(product);
                    ctrl.prodUnit.setValue(product.getUnit());
                    ctrl.imageView.setImage(new Image(product.getImgSrc()));
                    ctrl.prodName.setText(product.getName());
                    ctrl.prodPrice.setText(String.valueOf(product.getPrice()));
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
        btnDelProd.setLayoutX(66);
        btnDelProd.setLayoutY(260);
        btnDelProd.getStyleClass().addAll("btn", "btn-danger");
        pane.getChildren().add(btnDelProd);

        btnDelProd.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println(getClass() + "btn del prod clicked");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Xác nhận xoá sản phẩm " + product.getName() + "?");

                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get() == ButtonType.OK) {
                    try {
                        DatabaseCommunication dbc = DatabaseCommunication.getInstance();
                        DeleteQuery dq = new DeleteQuery();
                        dq.deleteFrom("PRODUCTS_IN_PACKAGES").where("product_id='" + product.getId() + "'");
                        dbc.execute(dq.getQuery());
                        try {
                            dq.clear();
                            dq.deleteFrom("PRODUCTS").where("product_id='" + product.getId() + "'");
                            dbc.execute(dq.getQuery());
                        } catch (SQLException e) {
                            e.printStackTrace();
                            throw e;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public Pane getPane() {
        return pane;
    }
}
