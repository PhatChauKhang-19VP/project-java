package pck.java.fe.admin;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pck.java.Index;

public class ManageTreatmentLocController {
    public Button logoutButton;
    public Button backButton;
    public TableView tableViewTreatmentLocation;
    public TableColumn colNO;
    public TableColumn colName;
    public TableColumn colCapacity;
    public TableColumn colCurrentRoom;
    public TableColumn colBtn;
    public Button btnAddTloc;

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
}
