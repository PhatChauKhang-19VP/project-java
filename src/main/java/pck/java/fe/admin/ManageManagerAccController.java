package pck.java.fe.admin;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pck.java.Index;

public class ManageManagerAccController {
    public Button logoutButton;
    public Button backButton;
    public TableView tableViewManager;
    public TableColumn colNO;
    public TableColumn colUsername;
    public TableColumn colName;
    public TableColumn colBtn;
    public Button addMngButton;

    public void onBackButtonCliked(ActionEvent actionEvent) throws Exception {
        if(actionEvent.getSource() == backButton) {
            Index.getInstance().gotoAdminHomePage();
        }
    }

    public void onLogoutButtonCliked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == logoutButton) {
            pck.java.Index.getInstance().gotoSignIn();
        }
    }

    public void onAddManagerButtonCliked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == addMngButton) {
            pck.java.Index.getInstance().gotoCreateManagerAccount();
        }
    }
}
