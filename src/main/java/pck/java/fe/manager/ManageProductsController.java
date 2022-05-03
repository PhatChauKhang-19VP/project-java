package pck.java.fe.manager;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import pck.java.Index;
import pck.java.be.app.App;
import pck.java.be.app.product.Product;
import pck.java.be.app.user.Manager;
import pck.java.fe.utils.ProductPane;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ManageProductsController implements Initializable {
    public Button logoutButton;
    public Button backButton;
    public Button btnAddProd;
    public GridPane gridPaneProduct;
    public HashMap<String, Product> productList;
    public TextField searchField;
    public Button searchButton;
    private int sorted = 0;  // 0: unsorted, 1: asc, 2: desc

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productList = ((Manager) App.getInstance().getCurrentUser()).getProducts();
        GridPane gp = gridPaneProduct;

        int row = 0, col = 0;
        for (String key : productList.keySet()) {
            Product product = productList.get(key);
            ProductPane productPane = new ProductPane(product);
            Pane pTemp = productPane.getPane();
            GridPane.setConstraints(pTemp, col, row);
            gp.getChildren().add(pTemp);
            col += 1;
            if (col == 4) {
                col = 0;
                row += 1;
            }
        }
        btnAddProd.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    System.out.println(getClass() + "btn add prod clicked");
                    Stage modalAddProd = new Stage();
                    FXMLLoader loader = new FXMLLoader(Index.class.getResource("manager.modalMngrAddProd.fxml"), null, new JavaFXBuilderFactory());
                    Parent root = loader.load();
                    modalAddProd.initOwner(Index.getInstance().getStage());
                    modalAddProd.setScene(new Scene(root));
                    modalAddProd.setTitle("Thêm sản phẩm");
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
    }

    public void onBackButtonCliked(ActionEvent actionEvent) throws Exception {
        if (actionEvent.getSource() == backButton) {
            Index.getInstance().gotoManagerHomePage();
        }
    }

    public void onLogoutButtonCliked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == logoutButton) {
            pck.java.Index.getInstance().gotoSignIn();
        }
    }

    public void onBtnAddProdClicked(ActionEvent ae) {
        try {
            System.out.println(getClass() + "btn add prod clicked");
            Stage modalAddProd = new Stage();
            FXMLLoader loader = new FXMLLoader(Index.class.getResource("manager.modalMngrAddProd.fxml"), null, new JavaFXBuilderFactory());
            Parent root = loader.load();
            modalAddProd.initOwner(Index.getInstance().getStage());
            modalAddProd.setScene(new Scene(root));
            modalAddProd.setTitle("Thêm nhu yếu phẩm");
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

    private void filterGridPane(String name) {
        GridPane gp = gridPaneProduct;
        gp.getChildren().clear();

        Integer row = 0, col = 0;
        for (String key : productList.keySet()) {
            Product product = productList.get(key);
            if (product.getName().toLowerCase().contains(name.toLowerCase()) || name.isEmpty()) {
                ProductPane productPane = new ProductPane(product);
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

    public void onBtnSortClicked(ActionEvent ae) {
        System.out.println(getClass() + " btn sort prod clicked");

        List<Product> productListByPrice = new ArrayList<>(productList.values());
        if (sorted == 0) {
            Collections.sort(productListByPrice, Comparator.comparing(Product::getPrice));
            sorted = 1;
        } else if (sorted == 1) {
            Collections.sort(productListByPrice, Comparator.comparing(Product::getPrice).reversed());
            sorted = 2;
        } else {
            sorted = 0;
        }

        GridPane gp = gridPaneProduct;
        gp.getChildren().clear();

        int row = 0, col = 0;
        for (Product product : productListByPrice) {
            ProductPane productPane = new ProductPane(product);
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
