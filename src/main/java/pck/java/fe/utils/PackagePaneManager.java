package pck.java.fe.utils;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import pck.java.Index;
import pck.java.be.app.product.Package;
import pck.java.database.DatabaseCommunication;
import pck.java.database.DeleteQuery;
import pck.java.fe.manager.modal.ModPkgController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class PackagePaneManager {
    private static final String iconMinusURL = "https://res.cloudinary.com/phatchaukhang/image/upload/v1649646897/HQTCSDL/Icon/minus-button_ukufql.png";
    private static final String iconPlusURL = "https://res.cloudinary.com/phatchaukhang/image/upload/v1649646899/HQTCSDL/Icon/plus_arj0go.png";
    Pane pane;
    Package pkg;

    public PackagePaneManager(Package pkg) {
        this.pkg = pkg;

        pane = new Pane();
        //pane.setBorder(ne);
        pane.setMinSize(252, 350);
        pane.setPrefSize(252, 350);

        //img view
        ImageView packageImg = new ImageView(new Image(pkg.getImg_src()));
        packageImg.setFitWidth(242);
        packageImg.setFitHeight(242);
        packageImg.setLayoutX(5);
        packageImg.setLayoutY(-10);
        pane.getChildren().add(packageImg);

        // label name
        Hyperlink packageName = new Hyperlink(pkg.getName());
        packageName.setAlignment(Pos.CENTER);
        packageName.setTextAlignment(TextAlignment.CENTER);
        packageName.setStyle("-fx-font-family: Arial;-fx-font-size:16; -fx-text-fill: #132ac1;");
        packageName.setPrefSize(200, 20);
        packageName.setLayoutX(26);
        packageName.setLayoutY(220);
        packageName.setAlignment(Pos.CENTER);
        packageName.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println(getClass() + " pakages detail");
                Index.getInstance().gotoPackageDetails(pkg.getId());
            }
        });
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

        // pane > btnModProd
        Button btnModPackage = new Button("Chỉnh sửa");
        btnModPackage.setAlignment(Pos.CENTER);
        btnModPackage.setPrefSize(120, 25);
        btnModPackage.setLayoutX(66);
        btnModPackage.setLayoutY(280);
        pane.getChildren().add(btnModPackage);

        btnModPackage.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    System.out.println(getClass() + "btn mod pkg clicked");
                    Stage modalAddProd = new Stage();
                    ModPkgController.setPkg(pkg);
                    FXMLLoader loader = new FXMLLoader(Index.class.getResource("manager.modalMngrModPkg.fxml"), null, new JavaFXBuilderFactory());
                    Parent root = loader.load();
                    modalAddProd.initOwner(Index.getInstance().getStage());
                    modalAddProd.setScene(new Scene(root));
                    modalAddProd.setTitle("Chỉnh sửa gói nhu yếu phẩm");
                    modalAddProd.initModality(Modality.APPLICATION_MODAL);

                    modalAddProd.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
                    modalAddProd.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                    modalAddProd.setResizable(false);
                    modalAddProd.setFullScreen(false);
                    modalAddProd.sizeToScene();
                    modalAddProd.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // pane > btnAddProd
        Button btnDeletePkg = new Button("Xoá gói");
        btnDeletePkg.setAlignment(Pos.CENTER);
        btnDeletePkg.setPrefSize(120, 25);
        btnDeletePkg.setLayoutX(66);
        btnDeletePkg.setLayoutY(310);
        pane.getChildren().add(btnDeletePkg);

        btnDeletePkg.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println(getClass() + " delete pakages");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Xác nhận xoá " + pkg.getName() + "?");

                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get() == ButtonType.OK) {
                    try {
                        DatabaseCommunication dbc = DatabaseCommunication.getInstance();
                        DeleteQuery dq = new DeleteQuery();
                        dq.deleteFrom("PRODUCTS_IN_PACKAGES").where("package_id='" + pkg.getId() + "'");
                        dbc.execute(dq.getQuery());
                        try {
                            dq.clear();
                            dq.deleteFrom("PACKAGES").where("package_id='" + pkg.getId() + "'");
                            dbc.execute(dq.getQuery());
                        } catch (SQLException e) {
                            e.printStackTrace();
                            throw e;
                        }
                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                        alert2.setTitle("Thông báo");
                        alert2.setHeaderText("Xoá gói sản phẩm thành công.");

                        Optional<ButtonType> option2 = alert2.showAndWait();

                        if (option2.isPresent() && option2.get() == ButtonType.OK) {
                            alert2.close();
                        }
                        alert.close();
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
