package pck.java.fe.manager;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import pck.java.Index;
import pck.java.be.app.user.Patient;

import java.io.IOException;

public class HomePageController {
    public Button logoutButton;
    public Button managePatientInfoButton;
    public Button manageProductsButton;
    public Button managePackagesButton;
    public Button statisticButton;

    public void onLogoutButtonCliked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == logoutButton) {
            pck.java.Index.getInstance().gotoSignIn();
        }
    }

    public void onManagePatientInfoButtonCliked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == managePatientInfoButton) {
            pck.java.Index.getInstance().gotoManagePatientInfo();
        }
    }

    public void onManageProductsButtonCliked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == manageProductsButton) {
            pck.java.Index.getInstance().gotoManagerProducts();
        }
    }
}
