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
import pck.java.be.app.product.Package;
import pck.java.be.app.user.Manager;
import pck.java.fe.utils.PackagePaneManager;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ManagePackagesController implements Initializable {
    public Button logoutButton;
    public Button backButton;
    public GridPane gridPanePackage;
    public Button btnAddPkg;
    public Button btnDEBUGmodPkg;
    public HashMap<String, Package> packageList;
    public TextField searchField;
    public Button searchButton;
    private int sorted = 0;  // 0: unsorted, 1: asc, 2: desc

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        packageList = ((Manager) App.getInstance().getCurrentUser()).getPackages();
        GridPane gp = gridPanePackage;

        int row = 0, col = 0;
        for (String key : packageList.keySet()) {
            Package pkg = packageList.get(key);
            PackagePaneManager packagePane = new PackagePaneManager(pkg);
            Pane pTemp = packagePane.getPane();

            GridPane.setConstraints(pTemp, col, row);
            gp.getChildren().add(pTemp);
            col += 1;
            if (col == 4) {
                col = 0;
                row += 1;
            }

        }

        btnAddPkg.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    System.out.println(getClass() + "btn add pkg clicked");
                    Stage modalAddProd = new Stage();
                    FXMLLoader loader = new FXMLLoader(Index.class.getResource("manager.modalMngrAddPkg.fxml"), null, new JavaFXBuilderFactory());
                    Parent root = loader.load();
                    modalAddProd.initOwner(Index.getInstance().getStage());
                    modalAddProd.setScene(new Scene(root));
                    modalAddProd.setTitle("Thêm gói nhu yếu phẩm");
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

        btnDEBUGmodPkg.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    System.out.println(getClass() + "btn mod pkg clicked");
                    Stage modalAddProd = new Stage();
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

    private void filterGridPane(String name) {
        GridPane gp = gridPanePackage;
        gp.getChildren().clear();

        Integer row = 0, col = 0;
        for (String key : packageList.keySet()) {
            Package pkg = packageList.get(key);
            if (pkg.getName().toLowerCase().contains(name.toLowerCase()) || name.isEmpty()) {
                PackagePaneManager productPane = new PackagePaneManager(pkg);
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

        List<Package> packageListByPrice = new ArrayList<>(packageList.values());
        if (sorted == 0) {
            Collections.sort(packageListByPrice, Comparator.comparing(Package::getPrice));
            sorted = 1;
        } else if (sorted == 1) {
            Collections.sort(packageListByPrice, Comparator.comparing(Package::getPrice).reversed());
            sorted = 2;
        } else {
            sorted = 0;
        }

        GridPane gp = gridPanePackage;
        gp.getChildren().clear();

        int row = 0, col = 0;
        for (Package pkg : packageListByPrice) {
            PackagePaneManager packagePane = new PackagePaneManager(pkg);
            Pane pTemp = packagePane.getPane();
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
