package pck.java.fe.patient;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import pck.java.Index;
import pck.java.be.app.App;

public class HomePageController {
    public Button infoButton;
    public Button buyPackageButton;
    public Button cartButton;
    public Button payBalanceButton;
    public Button logoutButton;

    public void onLogoutButtonCliked(ActionEvent actionEvent) {
        if(actionEvent.getSource() == logoutButton) {
            Index.getInstance().gotoSignIn();
        }
    }

    public void onInfoButtonCliked(ActionEvent actionEvent) {
        if(actionEvent.getSource() == infoButton) {
            Index.getInstance().gotoPatientInformation();
        }
    }

    public void onBuyPackageButtonCliked(ActionEvent actionEvent) {
        if(actionEvent.getSource() == buyPackageButton) {
            Index.getInstance().gotoBuyPackage();
        }
    }
}
