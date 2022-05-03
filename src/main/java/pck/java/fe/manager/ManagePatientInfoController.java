package pck.java.fe.manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import javafx.util.Pair;
import pck.java.Index;
import pck.java.be.app.App;
import pck.java.be.app.user.Manager;
import pck.java.be.app.user.Patient;
import pck.java.database.DatabaseCommunication;
import pck.java.fe.utils.LineNumbersCellFactory;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class ManagePatientInfoController implements Initializable {
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
    public TextField searchField;

    private final ObservableList<Patient> dataList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Manager mng = (Manager) App.getInstance().getCurrentUser();
        HashMap<String, Patient> patientList = mng.getPatients();

        DatabaseCommunication.getInstance().loadTreatmentLocations();

        TableView tableView = tableViewPatient;

        tableView.setRowFactory(param -> {
            return new TableRow() {
                @Override
                public void updateIndex(int i) {
                    super.updateIndex(i);
                    setTextAlignment(TextAlignment.JUSTIFY);
                    setMinHeight(70);
                }
            };
        });

        colNO.setCellFactory(new LineNumbersCellFactory<>());
        colName.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
        colUsername.setCellValueFactory(new PropertyValueFactory<Patient, String>("username"));
        colDob.setCellValueFactory(new PropertyValueFactory<Patient, String>("dobAsString"));
        colAddress.setCellValueFactory(new PropertyValueFactory<Patient, String>("addressAsString"));
        colStatus.setCellValueFactory(new PropertyValueFactory<Patient, String>("statusAsString"));
        colTLoc.setCellValueFactory(new PropertyValueFactory<Patient, String>("treatmentLocationAsString"));

        Callback<TableColumn<Patient, String>, TableCell<Patient, String>> cellFactoryMod
                =
                new Callback<>() {
                    @Override
                    public TableCell call(final TableColumn<Patient, String> param) {
                        final TableCell<Patient, String> cell = new TableCell<>() {

                            final Button btn = new Button("Sửa");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        System.out.println(getClass() + "btn mod Patient click");
                                    });
                                    btn.getStyleClass().addAll("btn", "btn-info");
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        colBtnMod.setCellFactory(cellFactoryMod);

        Callback<TableColumn<Patient, String>, TableCell<Patient, String>> cellFactoryDel
                =
                new Callback<>() {
                    @Override
                    public TableCell call(final TableColumn<Patient, String> param) {
                        final TableCell<Patient, String> cell = new TableCell<>() {

                            final Button btn = new Button("Xóa");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        System.out.println(getClass() + "btn ban Patient click");
                                    });
                                    btn.getStyleClass().addAll("btn", "btn-danger");
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        colBtnDel.setCellFactory(cellFactoryDel);


        for (String key : patientList.keySet()) {
            dataList.add(patientList.get(key));
        }

        FilteredList<Patient> filteredList = new FilteredList<>(dataList, b -> true);
        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    filteredList.setPredicate(patient -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();
                        if (patient.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                            return true;
                        } else if (patient.getUsername().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                            return true;
                        }
                        return false;
                    });
                });
        SortedList<Patient> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
    }

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
