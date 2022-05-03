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
import pck.java.database.DatabaseCommunication;
import pck.java.database.InsertQuery;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

public class AddProdController implements Initializable {
    public TextField prodName;
    public TextField prodPrice;
    public ImageView imageView;
    public Button btnSelectImg;
    public Button btnAddProduct;
    public ComboBox prodUnit;
    public ComboBox packageName;
    public TextField quantity;
    private String imgSrc;

    public Label invalidDetails;

    protected
    String successMessage = String.format("-fx-text-fill: GREEN;");
    String errorMessage = String.format("-fx-text-fill: RED;");
    String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
    String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prodUnit.getItems().addAll(App.getInstance().getProductUnitList());
        packageName.getItems().addAll(App.getInstance().getPackageNameList());
        btnSelectImg.setOnAction(
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

    public void onBtnAddProductClicked(ActionEvent ae) {
        boolean required = true;
        if (ae.getSource() == btnAddProduct) {
            if (prodName.getText().isBlank()) {
                required = false;
                invalidDetails.setText("Vui lòng điền đầy đủ thông tin.");
                prodName.setStyle(errorStyle);
                invalidDetails.setStyle(errorMessage);
                new animatefx.animation.Shake(prodName).play();
            } else {
                prodName.setStyle(successStyle);
            }
            if (prodPrice.getText().isBlank()) {
                required = false;
                invalidDetails.setText("Vui lòng điền đầy đủ thông tin.");
                prodPrice.setStyle(errorStyle);
                invalidDetails.setStyle(errorMessage);
                new animatefx.animation.Shake(prodPrice).play();
            } else if (Double.valueOf(prodPrice.getText()) <= 0) {
                required = false;
                prodPrice.setStyle(errorStyle);
                invalidDetails.setStyle(errorMessage);
                new animatefx.animation.Shake(prodPrice).play();
            } else {
                prodPrice.setStyle(successStyle);
            }
            if (quantity.getText().isBlank()) {
                required = false;
                invalidDetails.setText("Vui lòng điền đầy đủ thông tin.");
                quantity.setStyle(errorStyle);
                invalidDetails.setStyle(errorMessage);
                new animatefx.animation.Shake(quantity).play();
            } else if (Integer.valueOf(quantity.getText()) <= 0) {
                required = false;
                quantity.setStyle(errorStyle);
                invalidDetails.setStyle(errorMessage);
                new animatefx.animation.Shake(quantity).play();
            } else {
                quantity.setStyle(successStyle);
            }
            if (imgSrc == null || imgSrc.isBlank()) {
                required = false;
                invalidDetails.setText("Vui lòng điền đầy đủ thông tin.");
                btnSelectImg.setStyle(errorStyle);
                invalidDetails.setStyle(errorMessage);
                new animatefx.animation.Shake(btnSelectImg).play();
            } else {
                btnSelectImg.setStyle(successStyle);
            }
            if (prodUnit.getValue() == null) {
                required = false;
                invalidDetails.setText("Vui lòng điền đầy đủ thông tin.");
                prodUnit.setStyle(errorStyle);
                invalidDetails.setStyle(errorMessage);
                new animatefx.animation.Shake(prodUnit).play();
            } else {
                prodUnit.setStyle(successStyle);
            }
            if (packageName.getValue() == null) {
                required = false;
                invalidDetails.setText("Vui lòng điền đầy đủ thông tin.");
                packageName.setStyle(errorStyle);
                invalidDetails.setStyle(errorMessage);
                new animatefx.animation.Shake(packageName).play();
            } else {
                packageName.setStyle(successStyle);
            }
            if (required) {
                invalidDetails.setVisible(false);
                String name = prodName.getText(),
                        unit = String.valueOf(prodUnit.getValue()),
                        package_id = String.valueOf(packageName.getValue());
                package_id = package_id.split(" - ")[1];
                Double price = Double.valueOf(prodPrice.getText());
                Integer qty = Integer.valueOf(quantity.getText());
                String productID = "";
                do {
                    Random random = new Random();
                    productID = random.ints('a', 'z' + 1)
                            .limit(20)
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                            .toString();
                } while (App.getInstance().getProductManagement().getProductList().containsKey(productID));

                ArrayList<String> cols = new ArrayList<>();
                cols.add("product_id");
                cols.add("name");
                cols.add("img_src");
                cols.add("unit");
                cols.add("price");

                ArrayList<String> vals = new ArrayList<>();
                vals.add("'" + productID + "'");
                vals.add("N'" + name + "'");
                vals.add("'" + imgSrc + "'");
                vals.add("N'" + unit + "'");
                vals.add(String.valueOf(price));

                InsertQuery iq = new InsertQuery();
                iq.insertInto("PRODUCTS").columns(cols).values(vals);
                DatabaseCommunication dbc = DatabaseCommunication.getInstance();

                try {
                    dbc.execute(iq.getQuery());
                    try {
                        cols.clear();
                        cols.add("product_id");
                        cols.add("package_id");
                        cols.add("quantity");
                        vals.clear();
                        vals.add("'" + productID + "'");
                        vals.add("'" + package_id + "'");
                        vals.add(String.valueOf(qty));
                        iq.clear();
                        iq.insertInto("PRODUCTS_IN_PACKAGES").columns(cols).values(vals);
                        dbc.execute(iq.getQuery());
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw e;
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText("Tạo sản phẩm thành công.");
                    Optional<ButtonType> option = alert.showAndWait();
                    if (option.isPresent() && option.get() == ButtonType.OK) {
                        Stage stage = (Stage) prodName.getScene().getWindow();
                        stage.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText("Không thể tạo sản phẩm.");
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
