package pck.java.fe.patient;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import pck.java.Index;

public class BuyPackageController {
    public Button logoutButton;
    public Button backButton;
    public GridPane gridPanePackage;

    public void onBackButtonCliked(ActionEvent actionEvent) throws Exception {
        if(actionEvent.getSource() == backButton) {
            Index.getInstance().gotoPatientHomePage();
        }
    }

    public void onLogoutButtonCliked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == logoutButton) {
            pck.java.Index.getInstance().gotoSignIn();
        }
    }
}
