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
    public TableView<Patient> tableViewPatient;
    public TableColumn colNO;
    public TableColumn colName;
    public TableColumn colUsername;
    public TableColumn colDob;
    public TableColumn colAddress;
    public TableColumn colStatus;
    public TableColumn colTLoc;
    public TableColumn colBtnMod;
    public TableColumn colBtnDel;

    public GridPane gridPaneProduct;
    public GridPane gridPanePackage;
    public Button btnAddProd;
    public Button btnAddPkg;
    public Button btnDEBUGmodPkg;

}
