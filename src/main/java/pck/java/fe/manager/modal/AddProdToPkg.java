package pck.java.fe.manager.modal;

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
import javafx.stage.Stage;
import pck.java.Index;
import pck.java.be.app.App;
import pck.java.be.app.product.Product;
import pck.java.be.app.user.Manager;
import pck.java.be.app.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

class ProductPaneWithAddBtn {
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

    public ProductPaneWithAddBtn(Product product) {
        this.product = product;

        pane = new Pane();
        pane.setMinSize(252, 320);
        pane.setPrefSize(252, 320);

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
        quantityField.setPromptText("Số lượng");
        quantityField.setAlignment(Pos.CENTER);
        quantityField.setPrefSize(120, 25);
        quantityField.setLayoutX(66);
        quantityField.setLayoutY(220);
        pane.getChildren().add(quantityField);

        // pane > btnAddProd
        Button btnAddProd = new Button("Thêm");
        btnAddProd.setAlignment(Pos.CENTER);
        btnAddProd.setPrefSize(120, 25);
        btnAddProd.setLayoutX(66);
        btnAddProd.setLayoutY(250);
        pane.getChildren().add(btnAddProd);

        // label invalide details
        Label invalidDetails = new Label(String.format("%.3f VNĐ", product.getPrice()));
        invalidDetails.setTextAlignment(TextAlignment.CENTER);
        invalidDetails.setAlignment(Pos.CENTER);
        invalidDetails.setStyle("-fx-font-family: Arial;");
        invalidDetails.setPrefSize(200, 25);
        invalidDetails.setLayoutX(26);
        invalidDetails.setLayoutY(280);
        invalidDetails.setVisible(false);
        pane.getChildren().add(invalidDetails);

        btnAddProd.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println(getClass() + "btn mod prod clicked");
                if (quantityField.getText().isBlank()) {
                    invalidDetails.setVisible(true);
                    invalidDetails.setText("Vui lòng điền đầy đủ thông tin.");
                    quantityField.setStyle(errorStyle);
                    invalidDetails.setStyle(errorMessage);
                    new animatefx.animation.Shake(quantityField).play();
                } else if (!isNumeric(quantityField.getText()) || Integer.valueOf(quantityField.getText()) <= 0) {
                    invalidDetails.setVisible(true);
                    quantityField.setStyle(errorStyle);
                    invalidDetails.setStyle(errorMessage);
                    invalidDetails.setText("Vui lòng điền thông tin hợp lệ.");
                    new animatefx.animation.Shake(quantityField).play();
                } else {
                    invalidDetails.setVisible(false);
                    quantityField.setStyle(successStyle);

                    if (AddProdToPkg.adding) {
                        AddPkgController parentCtrl = new AddPkgController();
                        parentCtrl.productInPackage.put(product.getId(), product);
                        Integer oldValue = parentCtrl.productQuantity.get(product.getId());
                        if (oldValue == null)
                            oldValue = 0;
                        parentCtrl.productQuantity.put(
                                product.getId(),
                                oldValue + Integer.valueOf(quantityField.getText())
                        );
                    } else {
                        Integer oldValue;
                        if (ModPkgController.productInPackage.get(product.getId()) == null) {
                            oldValue = 0;
                        } else {
                            oldValue = ModPkgController.productInPackage.get(product.getId()).getSecond();
                        }
                        ModPkgController.productInPackage.put(
                                product.getId(),
                                new Pair<Product, Integer>(
                                        product, oldValue + Integer.valueOf(quantityField.getText())
                                )
                        );
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText("Thêm sản phẩm vào gói thành công!");

                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.isPresent() && option.get() == ButtonType.OK) {
                        alert.close();
                    }
                }
            }
        });
    }

    public Pane getPane() {
        return pane;
    }
}


public class AddProdToPkg implements Initializable {
    public static boolean adding;
    public Button backButton;
    public GridPane gridPaneProduct;
    public TextField searchField;
    public Button searchButton;
    private HashMap<String, Product> productList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productList = ((Manager) App.getInstance().getCurrentUser()).getProducts();
        GridPane gp = gridPaneProduct;

        int row = 0, col = 0;
        for (String key : productList.keySet()) {
            Product product = productList.get(key);
            ProductPaneWithAddBtn productPane = new ProductPaneWithAddBtn(product);
            Pane pTemp = productPane.getPane();
            GridPane.setConstraints(pTemp, col, row);
            gp.getChildren().add(pTemp);
            col += 1;
            if (col == 4) {
                col = 0;
                row += 1;
            }
        }
    }

    public void onBackButtonCliked(ActionEvent ae) {
        FXMLLoader loader = null;
        if (adding) {
            loader = new FXMLLoader(Index.class.getResource("manager.modalMngrAddPkg.fxml"), null, new JavaFXBuilderFactory());
        } else {
            loader = new FXMLLoader(Index.class.getResource("manager.modalMngrModPkg.fxml"), null, new JavaFXBuilderFactory());
        }
        Parent root = null;
        try {
            root = loader.load();
            Stage stage = (Stage) this.gridPaneProduct.getScene().getWindow();
            Scene scene = stage.getScene();
            stage.getScene().setRoot(root);
            stage.setResizable(false);
            stage.setFullScreen(false);
            stage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void filterGridPane(String name) {
        GridPane gp = gridPaneProduct;
        gp.getChildren().clear();

        Integer row = 0, col = 0;
        for (String key : productList.keySet()) {
            Product product = productList.get(key);
            if (product.getName().toLowerCase().contains(name.toLowerCase()) || name.isEmpty()) {
                ProductPaneWithAddBtn productPane = new ProductPaneWithAddBtn(product);
                Pane pTemp = productPane.getPane();
                GridPane.setConstraints(pTemp, col, row);
                gp.getChildren().add(pTemp);
                col += 1;
                if (col == 4) {
                    col = 0;
                    row += 1;
                }
            }
        }
    }

    public void onBtnSearchCliked(ActionEvent ae) {
        try {
            System.out.println(getClass() + " btn search prod clicked");
            String searchText = searchField.getText();
            filterGridPane(searchText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
