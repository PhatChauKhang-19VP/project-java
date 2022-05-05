package pck.java.fe.admin;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pck.java.be.app.user.Manager;

public class HomePageController {
    public TableView<Manager> tableViewManager;
    public TableColumn colNO;
    public TableColumn colName;
    public TableColumn colUsername;
    public TableColumn colBtn;
    public Button logoutButton;
    public Button createManagerAccButton;
    public Button treatmentLocButton;
    public Button manageManagerAccButton;

    public void onLogoutButtonCliked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == logoutButton) {
            pck.java.Index.getInstance().gotoSignIn();
        }
    }

    public void onCreateManagerButtonCliked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == createManagerAccButton) {
            pck.java.Index.getInstance().gotoCreateManagerAccount();
        }
    }

    public Button btnAddTloc;
    public Button btnAddMng;
    public void onManageManagerButtonCliked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == manageManagerAccButton) {
            pck.java.Index.getInstance().gotoManageManagerAccount();
        }
    }

    public void onManageTLocButtonCliked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == treatmentLocButton) {
            pck.java.Index.getInstance().gotoManageTreatmentLocation();
        }
    }
}
