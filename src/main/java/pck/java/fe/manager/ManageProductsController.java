package pck.java.fe.manager;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import pck.java.Index;

public class ManageProductsController {
    public Button logoutButton;
    public Button backButton;
    public Button btnAddProd;
    public GridPane gridPaneProduct;

    public void onBackButtonCliked(ActionEvent actionEvent) throws Exception {
        if(actionEvent.getSource() == backButton) {
            Index.getInstance().gotoManagerHomePage();
        }
    }

    public void onLogoutButtonCliked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == logoutButton) {
            pck.java.Index.getInstance().gotoSignIn();
        }
    }
}
