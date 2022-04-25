package pck.java.fe.admin;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import pck.java.Index;

public class CreateManagerAccController {
        public Button backButton;
        public Button logoutButton;
        public Button createAccButton;

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
