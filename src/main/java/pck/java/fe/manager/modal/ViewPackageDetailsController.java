package pck.java.fe.manager.modal;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.TextAlignment;
import pck.java.Index;
import pck.java.be.app.App;
import pck.java.be.app.product.Product;
import pck.java.be.app.user.Manager;
import pck.java.fe.utils.LineNumbersCellFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ViewPackageDetailsController implements Initializable {
    public Button logoutButton;
    public Button backButton;
    public TableView tableViewManager;
    public TableColumn colName;
    public TableColumn colNO;
    public TableColumn colID;
    public TableColumn colType;
    public TableColumn colUnit;
    public TableColumn colPrice;
    private static String packageID;

    public void setPackageID(String packageID) {
        this.packageID = packageID;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Manager mng = (Manager) App.getInstance().getCurrentUser();
        HashMap<String, Product> productList = mng.getProductsInPackage(packageID);

        TableView tableView = tableViewManager;
        tableView.setRowFactory(param -> {
            return new TableRow() {
                @Override
                public void updateIndex(int i) {
                    super.updateIndex(i);
                    setTextAlignment(TextAlignment.JUSTIFY);
                    setMinHeight(70);
                }
            };
        });

        colNO.setCellFactory(new LineNumbersCellFactory<>());
        colID.setCellValueFactory(new PropertyValueFactory<Product, String>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<Product, String>("imgSrc"));
        colUnit.setCellValueFactory(new PropertyValueFactory<Product, String>("unit"));
        colPrice.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));

        for (String key : productList.keySet()) {
            Product product = productList.get(key);
            tableViewManager.getItems().add(product);
        }
    }

    public void onBackButtonCliked(ActionEvent actionEvent) throws Exception {
        if (actionEvent.getSource() == backButton) {
            Index.getInstance().gotoManagerPackages();
        }
    }

    public void onLogoutButtonCliked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == logoutButton) {
            pck.java.Index.getInstance().gotoSignIn();
        }
    }
}
