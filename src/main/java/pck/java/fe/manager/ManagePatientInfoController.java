package pck.java.fe.manager;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pck.java.Index;

public class ManagePatientInfoController {
    public Button logoutButton;
    public Button backButton;
    public TableView tableViewPatient;
    public TableColumn colNO;
    public TableColumn colName;
    public TableColumn colUsername;
    public TableColumn colDob;
    public TableColumn colAddress;
    public TableColumn colStatus;
    public TableColumn colTLoc;
    public TableColumn colBtnMod;
    public TableColumn colBtnDel;

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
