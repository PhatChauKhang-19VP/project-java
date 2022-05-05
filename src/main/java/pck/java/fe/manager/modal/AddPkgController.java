package pck.java.fe.manager.modal;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pck.java.Index;
import pck.java.be.app.App;
import pck.java.be.app.product.Product;
import pck.java.database.DatabaseCommunication;
import pck.java.database.InsertQuery;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

class ProductPaneWithDeleteBtn {
    private static final String iconMinusURL = "https://res.cloudinary.com/phatchaukhang/image/upload/v1649646897/HQTCSDL/Icon/minus-button_ukufql.png";
    private static final String iconPlusURL = "https://res.cloudinary.com/phatchaukhang/image/upload/v1649646899/HQTCSDL/Icon/plus_arj0go.png";
    private String errorMessage = String.format("-fx-text-fill: RED;");
    private String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
    private String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");
    Pane pane;
    Product product;

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  // match a number with optional '-' and decimal.
    }

    public ProductPaneWithDeleteBtn(Product product) {
        this.product = product;

        pane = new Pane();
        pane.setMinSize(252, 300);
        pane.setPrefSize(252, 300);

        AddPkgController ctrl = new AddPkgController();

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

        // quantity
        TextField quantityField = new TextField();
        quantityField.setText(String.valueOf(ctrl.productQuantity.get(product.getId())));
        quantityField.setAlignment(Pos.CENTER);
        quantityField.setPrefSize(120, 25);
        quantityField.setLayoutX(66);
        quantityField.setLayoutY(220);
        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                quantityField.setText(oldValue);
            }
            if (newValue.isEmpty() || Integer.valueOf(newValue) <= 0) {
                quantityField.setText("1");
            } else {
                quantityField.setStyle(successStyle);
            }
        });
        pane.getChildren().add(quantityField);

        // pane > btnAddProd
        Button btnDelProd = new Button("Xoá khỏi gói");
        btnDelProd.setAlignment(Pos.CENTER);
        btnDelProd.setPrefSize(120, 25);
        btnDelProd.setLayoutX(66);
        btnDelProd.setLayoutY(250);
        pane.getChildren().add(btnDelProd);

        btnDelProd.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println(getClass() + "btn del prod from pkg clicked");
                AddPkgController.productInPackage.remove(product.getId());
                AddPkgController.productQuantity.remove(product.getId());
            }
        });
    }

    public Pane getPane() {
        return pane;
    }
}

public class AddPkgController implements Initializable {
    public static HashMap<String, Product> productInPackage = new HashMap<>();
    public static HashMap<String, Integer> productQuantity = new HashMap<>();

    public TextField pkgName;
    public TextField pkgPrice;
    public Button btnSelectImg;
    public TextField pkgPurchaseLim;
    public TextField pkgTimeLim;
    public Button btnAddProd;
    public ImageView imageView;
    public Button btnAddPkg;
    public GridPane gridPaneProduct;
    public Label invalidDetails;

    private String errorMessage = String.format("-fx-text-fill: RED;");
    private String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
    private String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");
    private String imgSrc;
    private static String packageID;

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public static void setPackageID(String packageID) {
        AddPkgController.packageID = packageID;
    }

    private void gridInitialize() {
        GridPane gp = gridPaneProduct;

        int row = 0, col = 0;
        for (String key : productInPackage.keySet()) {
            Product product = productInPackage.get(key);
            ProductPaneWithDeleteBtn productPane = new ProductPaneWithDeleteBtn(product);
            Pane pTemp = productPane.getPane();
            GridPane.setConstraints(pTemp, col, row);
            gp.getChildren().add(pTemp);
            col += 1;
            if (col == 3) {
                col = 0;
                row += 1;
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gridInitialize();
        if (productInPackage.isEmpty()) {
            btnAddPkg.setDisable(true);
        } else {
            btnAddPkg.setDisable(false);
        }
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

    public void onBtnAddProdClicked(ActionEvent ae) {
        AddProdToPkg ctrl = new AddProdToPkg();
        ctrl.adding = true;
        FXMLLoader loader = new FXMLLoader(Index.class.getResource("manager.modalMngrAddProdToPkg.fxml"), null, new JavaFXBuilderFactory());
        Parent root = null;
        try {
            root = loader.load();
            Stage stage = (Stage) pkgName.getScene().getWindow();
            Scene scene = stage.getScene();
            stage.getScene().setRoot(root);
            stage.setResizable(false);
            stage.setFullScreen(false);
            stage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onBtnAddPackageClicked(ActionEvent ae) {
        boolean required = true;
        do {
            Random random = new Random();
            packageID = random.ints('a', 'z' + 1)
                    .limit(20)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        } while (App.getInstance().getProductManagement().getPackageList().containsKey(packageID));
        if (pkgName.getText().isBlank()) {
            required = false;
            invalidDetails.setVisible(true);
            pkgName.setStyle(errorStyle);
            new animatefx.animation.Shake(pkgName).play();
        } else {
            pkgName.setStyle(successStyle);
        }
        if (pkgPrice.getText().isBlank() || !isNumeric(pkgPrice.getText()) || Double.valueOf(pkgPrice.getText()) <= 0) {
            required = false;
            pkgPrice.setStyle(errorStyle);
            invalidDetails.setVisible(true);
            new animatefx.animation.Shake(pkgPrice).play();
        } else {
            pkgPrice.setStyle(successStyle);
        }
        if (pkgPurchaseLim.getText().isBlank() || !isNumeric(pkgPurchaseLim.getText()) || Integer.valueOf(pkgPurchaseLim.getText()) <= 0) {
            required = false;
            invalidDetails.setVisible(true);
            pkgPurchaseLim.setStyle(errorStyle);
            new animatefx.animation.Shake(pkgPurchaseLim).play();
        } else {
            pkgPurchaseLim.setStyle(successStyle);
        }
        if (pkgTimeLim.getText().isBlank() || !isNumeric(pkgTimeLim.getText()) || Integer.valueOf(pkgTimeLim.getText()) <= 0) {
            required = false;
            invalidDetails.setVisible(true);
            pkgTimeLim.setStyle(errorStyle);
            new animatefx.animation.Shake(pkgTimeLim).play();
        } else {
            pkgTimeLim.setStyle(successStyle);
        }
        if (imgSrc == null || imgSrc.isBlank()) {
            required = false;
            invalidDetails.setVisible(true);
            btnSelectImg.setStyle(errorStyle);
            new animatefx.animation.Shake(btnSelectImg).play();
        } else {
            btnSelectImg.setStyle(successStyle);
        }

        if (required) {
            invalidDetails.setVisible(false);
            try {
                DatabaseCommunication dbc = DatabaseCommunication.getInstance();
                InsertQuery iq = new InsertQuery();

                Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", "phatchaukhang",
                        "api_key", "471685925227765",
                        "api_secret", "L--pAliKsFYLbtu2pXa_mAeZQio"));
                try {
                    Map uploader = cloudinary.uploader().upload(new File(imgSrc), ObjectUtils.emptyMap());
                    imgSrc = (String) uploader.get("secure_url");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ArrayList<String> cols = new ArrayList<>();
                cols.add("package_id");
                cols.add("name");
                cols.add("img_src");
                cols.add("purchased_amount_limit");
                cols.add("time_limit");
                cols.add("price");

                ArrayList<String> vals = new ArrayList<>();
                vals.add("'" + packageID + "'");
                vals.add("N'" + pkgName.getText() + "'");
                vals.add("'" + imgSrc + "'");
                vals.add(pkgPurchaseLim.getText());
                vals.add(pkgTimeLim.getText());
                vals.add(pkgPrice.getText());

                iq.insertInto("PACKAGES").columns(cols).values(vals);
                dbc.execute(iq.getQuery());

                cols.clear();
                cols.add("package_id");
                cols.add("product_id");
                cols.add("quantity");
                productQuantity.forEach((productID, quantity) -> {
                    vals.clear();
                    vals.add("'" + packageID + "'");
                    vals.add("'" + productID + "'");
                    vals.add(String.valueOf(quantity));
                    iq.clear();
                    iq.insertInto("PRODUCTS_IN_PACKAGES").columns(cols).values(vals);
                    try {
                        dbc.execute(iq.getQuery());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Tạo gói thành công.");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.isPresent() && option.get() == ButtonType.OK) {
                    Stage stage = (Stage) pkgName.getScene().getWindow();
                    stage.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Không thể tạo gói nhu yếu phẩm.");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.isPresent() && option.get() == ButtonType.OK) {
                    Stage stage = (Stage) pkgName.getScene().getWindow();
                    stage.close();
                }
            } finally {
                packageID = "";
                productInPackage.clear();
            }
        }
    }
}
