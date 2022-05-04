package pck.java.fe.manager.modal;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pck.java.Index;
import pck.java.be.app.App;
import pck.java.be.app.user.Manager;
import pck.java.be.app.user.Patient;
import pck.java.be.app.util.*;
import pck.java.database.DatabaseCommunication;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ModPatientController implements Initializable {
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
    public Button btnUpdatePatient;

    protected
    String successMessage = String.format("-fx-text-fill: GREEN;");
    String errorMessage = String.format("-fx-text-fill: RED;");
    String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
    String successStyle = String.format("-fx-border-color: GREEN; -fx-border-width: 2; -fx-border-radius: 5;");


    //data
    public Patient patient = null;
    private HashMap<String, Province> provinceList;
    private HashMap<String, District> districtList;
    private HashMap<String, Ward> wardList;
    private HashMap<String, TreatmentLocation> tlocList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
                //tfID.setText(oldValue);
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
    }

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

        if (!tfFStat.getText().matches("f\\d+|F\\d+")) {
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
            paneLeft.setStyle(errorStyle);
            new animatefx.animation.Shake(paneLeft).play();
        } else {
        }

        return isValidate;
    }

    public void onBtnUpdatePatientClick(ActionEvent ae) {
        if (ae.getSource() == btnUpdatePatient){
            if (isPatientFormValidate()){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Cập nhật bệnh nhân");
                alert.setHeaderText("Xác nhận cập nhật thông tin bệnh nhân ?");

                // option != null.
                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get() == ButtonType.OK) {
                    Manager manager = (Manager) App.getInstance().getCurrentUser();
                    int newFStatus = Integer.parseInt(tfFStat.getText().substring(1));
                    if (newFStatus < patient.getStatus() && newFStatus >= 0){

                        if (!manager.updateStatus(patient, newFStatus)){
                            Alert alert1 = new Alert(Alert.AlertType.WARNING);
                            alert1.setTitle("Thông báo");
                            alert1.setHeaderText("Cập nhật thất bại");
                        }
                    }

                    TreatmentLocation tloc = cbTloc.getValue();
                    if (!tloc.getCode().equals(patient.getTreatmentLocation().getCode())){

                        if (!manager.updateTloc(patient, tloc)){
                            Alert alert1 = new Alert(Alert.AlertType.WARNING);
                            alert1.setTitle("Thông báo");
                            alert1.setHeaderText("Cập nhật thất bại");
                            return;
                        }
                    }

                    patient.setName(tfName.getText());
                    patient.setDob(tfDob.getValue());
                    patient.setAddress(new Location(
                            tfAL.getText(),
                            cbWard.getValue(),
                            cbDistrict.getValue(),
                            cbProvince.getValue()
                    ));

                    manager.updatePatient(patient);
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Thông báo");
                    alert1.setHeaderText("Cập nhật thành công");

                    Optional<ButtonType> option2 = alert1.showAndWait();

                    if (option2.isPresent() && option2.get() == ButtonType.OK){
                        ((Stage)btnUpdatePatient.getScene().getWindow()).close();
                        Index.getInstance().gotoManagePatientInfo();
                    }

                }
            }
        }
    }
}
