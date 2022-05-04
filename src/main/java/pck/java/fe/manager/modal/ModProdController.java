package pck.java.fe.manager.modal;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pck.java.Index;
import pck.java.be.app.App;
import pck.java.be.app.product.Product;
import pck.java.database.DatabaseCommunication;
import pck.java.database.UpdateQuery;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModProdController implements Initializable {
    public ImageView imageView;
    public TextField prodName;
    public TextField prodPrice;
    public Button btnSelectImage;
    public ComboBox prodUnit;
    public Button btnUpdateProduct;
    private Product product;
    private String imgSrc;

    public Label invalidDetails;

    protected
    String successMessage = String.format("-fx-text-fill: GREEN;");
    String errorMessage = String.format("-fx-text-fill: RED;");
    String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
    String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prodUnit.getItems().addAll(App.getInstance().getProductUnitList());
        btnSelectImage.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        FileChooser fileChooser = new FileChooser();
                        File file = fileChooser.showOpenDialog(Index.getInstance().getStage());
                        imgSrc = file.getAbsolutePath();
                        if (file != null) {
                            imageView.setImage(new Image(imgSrc));
                        }
                    }
                });
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void onBtnUpdateProductClicked(ActionEvent ae) {
        boolean required = false;
        if (ae.getSource() == btnUpdateProduct) {
            if (!isNumeric(prodPrice.getText()) || Double.valueOf(prodPrice.getText()) <= 0) {
                required = false;
                prodPrice.setStyle(errorStyle);
                invalidDetails.setStyle(errorMessage);
                invalidDetails.setText("Giá bán không hợp lệ.");
                new animatefx.animation.Shake(prodPrice).play();
            } else {
                required = true;
                prodPrice.setStyle(successStyle);
            }
            if (required) {
                invalidDetails.setVisible(false);
                String newName = prodName.getText(),
                        newUnit = String.valueOf(prodUnit.getValue());
                Double newPrice = Double.valueOf(prodPrice.getText());

                UpdateQuery uq = new UpdateQuery();
                uq.update("PRODUCTS").where("product_id='" + product.getId() + "'");
                if (!newName.isBlank()) {
                    uq.set("name=N'" + newName + "'");
                }
                if (prodUnit.getValue() != null) {
                    uq.set("unit=N'" + newUnit + "'");
                }
                if (imgSrc != null && !imgSrc.isBlank()) {
                    uq.set("img_src='" + imgSrc + "'");
                }
                if (!prodPrice.getText().isBlank()) {
                    newPrice = Double.valueOf(prodPrice.getText());
                    uq.set("price=" + newPrice);
                }
                DatabaseCommunication dbc = DatabaseCommunication.getInstance();

                try {
                    dbc.execute(uq.getQuery());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText("Cập nhật sản phẩm thành công.");
                    Optional<ButtonType> option = alert.showAndWait();
                    if (option.isPresent() && option.get() == ButtonType.OK) {
                        Stage stage = (Stage) prodName.getScene().getWindow();
                        stage.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText("Đã có lỗi xảy ra!");
                    Optional<ButtonType> option = alert.showAndWait();
                    if (option.isPresent() && option.get() == ButtonType.OK) {
                        Stage stage = (Stage) prodName.getScene().getWindow();
                        stage.close();
                    }
                }
            }
        }
    }
}
