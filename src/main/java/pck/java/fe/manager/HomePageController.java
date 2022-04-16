package pck.java.fe.manager;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import pck.java.be.app.user.Patient;

public class HomePageController {
    public TableView<Patient> tableViewPatient;
    public TableColumn colNO;
    public TableColumn colName;
    public TableColumn colUsername;
    public TableColumn colDob;
    public TableColumn colAddress;
    public TableColumn colStatus;
    public TableColumn colTLoc;
    public TableColumn colBtn;

    public GridPane gridPaneProduct;
    public GridPane gridPanePackage;
}
