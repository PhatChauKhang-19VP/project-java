package pck.java.fe.patient;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pck.java.Index;
import pck.java.be.app.user.Patient;
import pck.java.be.app.util.HistoryDetail;

public class PaymentHistoryController {
    public Button logoutButton;
    public Button backButton;
    public TableView<HistoryDetail> tableViewHistory;
    public TableColumn colNO;
    public TableColumn<HistoryDetail, String> colUsername;
    public TableColumn<HistoryDetail, String> colTime;
    public TableColumn<HistoryDetail, String> colContent;

    public void onBackButtonCliked(ActionEvent actionEvent) throws Exception {
        if(actionEvent.getSource() == backButton) {
            Index.getInstance().gotoPatientInformation();
        }
    }

    public void onLogoutButtonCliked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == logoutButton) {
            pck.java.Index.getInstance().gotoSignIn();
        }
    }
}
