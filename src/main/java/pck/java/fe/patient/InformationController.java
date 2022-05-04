package pck.java.fe.patient;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import pck.java.Index;

public class InformationController {
    public Button backButton;
    public Button logoutButton;
    public Button managedHistoryBtn;
    public Button consumptionHistoryBtn;
    public Button paymentHistoryBtn;

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

    public void onManagedHistoryButtonCliked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == managedHistoryBtn) {
            pck.java.Index.getInstance().gotoManagedHistory();
        }
    }

    public void onConsumptionHistoryButtonCliked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == consumptionHistoryBtn) {
            pck.java.Index.getInstance().gotoConsumptionHistory();
        }
    }

    public void onPaymentHistoryButtonCliked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == paymentHistoryBtn) {
            pck.java.Index.getInstance().gotoPaymentHistory();
        }
    }
}
