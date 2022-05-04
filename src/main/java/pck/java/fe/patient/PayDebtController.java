package pck.java.fe.patient;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import pck.java.Index;
import pck.java.be.app.App;
import pck.java.be.app.user.Patient;
import pck.java.database.DatabaseCommunication;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PayDebtController  implements Initializable {
    public Button logoutButton;
    public Button backButton;
    public Button btnPayDebt;
    public TextField tfAmount;
    public PasswordField pwdf;
    public TextField tfName;
    public TextField tfID;
    public TextField tfDob;
    public TextField tfDebitBalance;
    public TextField tfUsername;

    public Patient patient;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                //emID.setVisible(true);
            } else {

            }
        });

        DatabaseCommunication.getInstance().loadPatients();

        patient = (Patient) App.getInstance().getUserList().get(App.getInstance().getCurrentUser().getUsername());

        tfName.setText(patient.getName());
        tfDob.setText(patient.getDobAsString());
        tfDebitBalance.setText("NULL");
        tfUsername.setText(patient.getUsername());
        tfID.setText(patient.getUsername());
    }

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

    public void onBtnPayDebtClick(ActionEvent actionEvent){
        if (actionEvent.getSource() == btnPayDebt){
            //todo paydept
            Patient p = (Patient) App.getInstance().getCurrentUser();

            boolean res = p.payBill(pwdf.getText(), Integer.parseInt(tfAmount.getText()));

            if (res){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Thanh toán dư nợ thành công\n" + "amount: " + tfAmount.getText());

                // option != null.
                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get() == ButtonType.OK) {

                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Thanh toán dư nợ thành công\n" + "amount: " + tfAmount.getText());

            }
        }
    }
}
