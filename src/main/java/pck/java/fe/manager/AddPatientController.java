package pck.java.fe.manager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import pck.java.Index;
import pck.java.be.app.App;
import pck.java.be.app.user.IUser;
import pck.java.be.app.user.Manager;
import pck.java.be.app.user.Patient;
import pck.java.be.app.user.UserConcreteComponent;
import pck.java.be.app.util.*;
import pck.java.database.DatabaseCommunication;
import pck.java.fe.utils.LineNumbersCellFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddPatientController implements Initializable {
    //stage
    public Stage stage;
    public FXMLLoader loader;
    // Components
    public Label lblPrimary;
    public int f;
    public Button btnSignOut;
    public Button btnBack;
    public SplitPane splitPane;
    public Pane paneLeft;
    public TextField tfName;
    public Label emName;
    public TextField tfID;
    public Label emID;
    public DatePicker tfDob;
    public Label emDob;
    public TextField tfFStat;
    public Label emFStat;
    public ComboBox<TreatmentLocation> cbTloc;
    public Label emTloc;
    public ComboBox<Province> cbProvince;
    public Label emProvince;
    public ComboBox<District> cbDistrict;
    public Label emDistrict;
    public ComboBox<Ward> cbWard;
    public Label emWard;
    public TextField tfAL;
    public Label emAL;
    public Pane paneRight;
    public Button btnAddCloseContact;
    public Button btnAddPatient;
    public TableView<Patient> tvCloseContacts;
    public TableColumn<Patient, String> colNo;
    public TableColumn<Patient, String> colID;
    public TableColumn<Patient, String> colDob;
    public TableColumn<Patient, String> colFStat;
    public TableColumn<Patient, String> colAddress;
    public TableColumn<Patient, String> colBtnDelete;
    public TableColumn<Patient, String> colBtnDetail;
    protected
    String successMessage = String.format("-fx-text-fill: GREEN;");
    String errorMessage = String.format("-fx-text-fill: RED;");
    String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
    String successStyle = String.format("-fx-border-color: GREEN; -fx-border-width: 2; -fx-border-radius: 5;");
    //Data
    Patient patientUpper = null;
    Patient patientCurr = null;
    private HashMap<String, Province> provinceList;
    private HashMap<String, District> districtList;
    private HashMap<String, Ward> wardList;
    private HashMap<String, TreatmentLocation> tlocList;

    private boolean isPatientFormValidate() {
        boolean isValidate = true;
        if (tfID.getText().isEmpty()) {
            isValidate = false;
            tfID.setStyle(errorStyle);
            emID.setStyle(errorMessage);
            emID.setVisible(true);
            new animatefx.animation.Shake(tfID).play();
        }
        if (tfName.getText().isEmpty()) {
            isValidate = false;
            tfName.setStyle(errorStyle);
            emName.setStyle(errorMessage);
            emName.setVisible(true);
            new animatefx.animation.Shake(tfName).play();
        }

        if (!tfFStat.getText().matches("f\\d+|F\\d+")){
            isValidate = false;
            tfFStat.setStyle(errorStyle);
            emFStat.setStyle(errorMessage);
            emFStat.setVisible(true);
            new animatefx.animation.Shake(tfFStat).play();
        }

        if (tfDob.getValue() == null) {
            isValidate = false;
            tfDob.setStyle(errorStyle);
            emDob.setStyle(errorMessage);
            emDob.setVisible(true);
            new animatefx.animation.Shake(tfDob).play();
        }

        if (cbTloc.getValue() == null) {
            isValidate = false;
            cbTloc.setStyle(errorStyle);
            emTloc.setStyle(errorMessage);
            emTloc.setVisible(true);
            new animatefx.animation.Shake(cbTloc).play();
        }

        if (cbProvince.getValue() == null) {
            isValidate = false;
            cbProvince.setStyle(errorStyle);
            emProvince.setStyle(errorMessage);
            emProvince.setVisible(true);
            new animatefx.animation.Shake(cbProvince).play();
        }

        if (cbDistrict.getValue() == null) {
            isValidate = false;
            cbDistrict.setStyle(errorStyle);
            emDistrict.setStyle(errorMessage);
            emDistrict.setVisible(true);
            new animatefx.animation.Shake(cbDistrict).play();
        }

        if (cbWard.getValue() == null) {
            isValidate = false;
            cbWard.setStyle(errorStyle);
            emWard.setStyle(errorMessage);
            emWard.setVisible(true);
            new animatefx.animation.Shake(cbWard).play();
        }

        if (tfAL.getText() == null) {
            isValidate = false;
            tfAL.setStyle(errorStyle);
            emAL.setStyle(errorMessage);
            emAL.setVisible(true);
            new animatefx.animation.Shake(tfAL).play();
        }
        if (!isValidate) {
            //paneLeft.setStyle(errorStyle);
            new animatefx.animation.Shake(paneLeft).play();
        } else {
            //create a patient with input information
            if (patientCurr == null) {
                patientCurr = new Patient(
                        new UserConcreteComponent(tfID.getText(), tfName.getText(), tfID.getText(), IUser.Role.PATIENT),
                        f,
                        tfDob.getValue(),
                        new Location(tfAL.getText(), cbWard.getValue(), cbDistrict.getValue(), cbProvince.getValue()),
                        cbTloc.getValue(),
                        new ArrayList<>()
                );

                if (patientUpper != null) {
                    patientUpper.addCloseContact(patientCurr);
                }
            } else {
                patientCurr.setUsername(tfID.getText());
                patientCurr.setName(tfName.getText());
                patientCurr.setDob(tfDob.getValue());
                patientCurr.setAddress(new Location(tfAL.getText(), cbWard.getValue(), cbDistrict.getValue(), cbProvince.getValue()));
                patientCurr.setTreatmentLocation(cbTloc.getValue());
            }
        }

        return isValidate;
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {

        // setup table view
        Callback<TableColumn<Patient, String>, TableCell<Patient, String>> cellFactoryBtnDel = new Callback<>() {
            @Override
            public TableCell<Patient, String> call(final TableColumn<Patient, String> param) {
                return new TableCell<>() {

                    final Button btn = new Button("Xóa");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                System.out.println(getClass() + "btn delete close contacts click");
                            });
                            btn.getStyleClass().addAll("btn", "btn-danger");
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
            }
        };

        Callback<TableColumn<Patient, String>, TableCell<Patient, String>> cellFactoryBtnDetail = new Callback<>() {
            @Override
            public TableCell<Patient, String> call(final TableColumn<Patient, String> param) {
                return new TableCell<>() {

                    final Button btn = new Button("Sửa");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                System.out.println(getClass() + "btn detail close contacts click");
                            });
                            btn.getStyleClass().addAll("btn", "btn-info");
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
            }
        };

        tvCloseContacts.setRowFactory(param -> new TableRow<>() {
            @Override
            public void updateIndex(int i) {
                super.updateIndex(i);
                setTextAlignment(TextAlignment.JUSTIFY);
                setMinHeight(70);
            }
        });

        lblPrimary.setText("Thêm người liên quan Covid-19 - F" + f);
        tfFStat.setText("F" + f);

        colNo.setCellFactory(new LineNumbersCellFactory<>());
        colID.setCellValueFactory(new PropertyValueFactory<>("username"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dobAsString"));
        colFStat.setCellValueFactory(new PropertyValueFactory<>("status"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("addressAsString"));
        colBtnDelete.setCellFactory(cellFactoryBtnDel);
        colBtnDetail.setCellFactory(cellFactoryBtnDetail);

        // event text change for text fields
        tfName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                emName.setVisible(true);
            } else {
                tfName.setStyle(successStyle);
                emName.setVisible(false);
            }
        });
        tfID.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfID.setText(oldValue);
            }
            if (newValue.isEmpty()) {
                emID.setVisible(true);
            } else {
                tfID.setStyle(successStyle);
                emID.setVisible(false);
            }
        });

        // load data for Administrative Division
        DatabaseCommunication.getInstance().loadAdministrativeDivisions();
        provinceList = App.getInstance().getProvinceList();
        districtList = App.getInstance().getDistrictList();
        wardList = App.getInstance().getWardList();

        // load data for treatment location
        DatabaseCommunication.getInstance().loadTreatmentLocations();
        tlocList = App.getInstance().getTreatmentLocationList();

        //set up data and event for combo boxes
        cbTloc.getItems().addAll(tlocList.values());
        cbTloc.setOnAction((e) -> {

            TreatmentLocation selectedItem = cbTloc.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                return;
            }
            cbTloc.setStyle(successStyle);
            emTloc.setVisible(false);
        });

        tfDob.setOnAction((e) -> {
            tfDob.setStyle(successStyle);
            emDob.setVisible(false);
        });

        cbProvince.getItems().addAll(provinceList.values());
        cbProvince.setOnAction((e) -> {
            cbDistrict.getItems().clear();
            cbWard.getItems().clear();
            cbWard.setDisable(true);

            Province selectedItem = cbProvince.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                return;
            }

            cbProvince.setStyle(successStyle);
            emProvince.setVisible(false);

            Map<String, District> dt = districtList.entrySet()
                    .stream()
                    .filter(map -> map.getValue().getBelong_to().getCode().equals(selectedItem.getCode()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            //LinkedHashMap<String, District> =
            cbDistrict.getItems().addAll(dt.values());

            cbDistrict.setDisable(false);
        });

        cbDistrict.setOnAction((e) -> {
            District selectedItem = cbDistrict.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                return;
            }

            cbDistrict.setStyle(successStyle);
            emDistrict.setVisible(false);

            cbWard.getItems().clear();
            cbWard.setPromptText("Chọn quận/huyện/thị trấn");

            Map<String, Ward> w = wardList.entrySet()
                    .stream()
                    .filter(map -> map.getValue().getBelong_to().getCode().equals(selectedItem.getCode()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            cbWard.getItems().addAll(w.values());

            cbWard.setDisable(false);
        });
        cbDistrict.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(District item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn quận/huyện/thị trấn");
                } else {
                    setText(item.getName());
                }
            }
        });

        cbWard.setOnAction((e) -> {
            District selectedItem = cbDistrict.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                return;
            }
            cbWard.setStyle(successStyle);
            cbWard.setDisable(false);
            emWard.setVisible(false);
        });
        cbWard.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Ward item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn xã/phường/thị trấn");
                } else {
                    setText(item.getName());
                }
            }
        });

        //create new thread for update table close contact
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        //System.out.println("F" + f);
                        if (patientCurr != null && tvCloseContacts.getItems().size() < patientCurr.getCloseContacts().size()) {
                            tvCloseContacts.getItems().clear();
                            tvCloseContacts.getItems().addAll(patientCurr.getCloseContacts());
                            System.out.println("add new");
                        }
                        if (!stage.isShowing()){
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        t.start();
        System.out.println("init add patient");

    }

    public void onBtnAddCloseContactClick(ActionEvent ae) {
        if (ae.getSource() == btnAddCloseContact) {
            System.out.println(getClass() + ": btnCloseContact clicked");
            if (isPatientFormValidate()) {
                // open another stage to add f_n+1
                try {
                    Stage stageCloseContact = new Stage();
                    FXMLLoader loader = new FXMLLoader(Index.class.getResource("manager.addPatient.fxml"), null, new JavaFXBuilderFactory());
                    Parent root = loader.load();
                    stageCloseContact.initOwner(stage);
                    stageCloseContact.initModality(Modality.APPLICATION_MODAL);
                    stageCloseContact.setScene(new Scene(root));
                    stage.setTitle("Quản lý thông tin covid-19");
                    //stage.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                    stage.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649057250/JAVA/Icon/covid-test-purple-128_za151v.png"));
                    stage.setResizable(false);
                    stage.setFullScreen(false);
                    stage.sizeToScene();

                    AddPatientController ctrl = loader.getController();
                    ctrl.stage = stageCloseContact;
                    ctrl.loader = loader;
                    ctrl.patientUpper = patientCurr;
                    ctrl.f = f + 1;
                    ctrl.lblPrimary.setText("Thêm người liên quan Covid-19 - F" + ctrl.f);
                    ctrl.tfFStat.setText("F" + f);

                    stageCloseContact.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onBtnAddPatientClick(ActionEvent ae) {
        if (ae.getSource() == btnAddPatient) {
            System.out.println(getClass() + ": btnAddPatient clicked");

            if (!isPatientFormValidate()) {
                return;
            }
            else {
                if (f == 0){
                    addPatientRecursive(patientCurr);
                    //Manager manager = (Manager) App.getInstance().getCurrentUser();
                    //manager.up
                }
                else {
                    stage.close();
                }
            }
        }
    }
    
    public boolean addPatientRecursive(Patient p){
        Manager manager = (Manager) App.getInstance().getCurrentUser();
        manager.addPatient(p);
        if (p.getTreatmentLocation().addPatient()){
            return false;
        }

        boolean ret = true;
        for (Patient p1 : p.getCloseContacts()){
           if ( addPatientRecursive(p1)||
                   manager.addCloseContact(p, p1)){
               ret = false;
           }
        }

        return ret;
    }

    public void onBackButtonCliked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnBack) {
            Index.getInstance().gotoManagerHomePage();
        }
    }

    public void onLogoutButtonCliked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnSignOut) {
            pck.java.Index.getInstance().gotoSignIn();
        }
    }
}
