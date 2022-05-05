package pck.java;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import pck.java.be.app.App;
import pck.java.be.app.product.Order;
import pck.java.be.app.product.Product;
import pck.java.be.app.util.History;
import pck.java.be.app.util.HistoryDetail;
import pck.java.be.app.util.Pair;
import pck.java.database.DatabaseCommunication;
import pck.java.be.app.product.Package;
import pck.java.be.app.util.TreatmentLocation;
import pck.java.database.DatabaseCommunication;
import pck.java.database.SelectQuery;
import pck.java.fe.mainPage.changeManagerPassController;
import pck.java.fe.manager.AddPatientController;
import pck.java.fe.manager.modal.ViewPackageDetailsController;
import pck.java.fe.patient.BuyPackageController;
import pck.java.fe.patient.CartController;
import pck.java.fe.manager.ManagePatientInfoController;
import pck.java.fe.patient.*;
import pck.java.fe.utils.LineNumbersCellFactory;
import pck.java.fe.utils.PackagePane;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class Index extends Application {
    private static Index instance;
    private Stage stage;
    private FXMLLoader loader;

    public Index() {
        instance = this;
    }

    public static Index getInstance() {
        return instance;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            stage = primaryStage;
            DatabaseCommunication dbc = DatabaseCommunication.getInstance();
            SelectQuery sq = new SelectQuery();
            sq.select("1").from("login_infos");
            List<Map<String, Object>> rs = dbc.executeQuery(sq.getQuery());
            if (rs.size() == 0) {
                gotoCreateAdminAccount();
            } else {
                gotoSignIn();
            }
            primaryStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // getters and setters
    public Stage getStage() {
        return stage;
    }

    public FXMLLoader getLoader() {
        return loader;
    }

    // mainPage
    public void gotoSignIn() {
        try {
            replaceSceneContent("mainPage.loginPage.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoCreateAdminAccount() {
        try {
            replaceSceneContent("mainPage.createAdminAccount.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoFirstStart() {
        try {
            replaceSceneContent("mainPage.firstStart.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoChangeManagerPassword(String username) {
        try {
            replaceSceneContent("mainPage.changeManagerPassword.fxml");
            changeManagerPassController ctrl = loader.getController();
            ctrl.username = username;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Admin
    public void gotoAdminHomePage() {
        try {
            replaceSceneContent("admin.homePage.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoCreateManagerAccount() {
        try {
            replaceSceneContent("admin.createManagerAccount.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoManageManagerAccount() {
        try {
            replaceSceneContent("admin.manageManagerAccount.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoManageTreatmentLocation() {
        try {
            replaceSceneContent("admin.manageTreatmentLocation.fxml");
            pck.java.fe.admin.ManageTreatmentLocController controller = loader.getController();

            DatabaseCommunication.getInstance().loadTreatmentLocations();
            controller.colNO.setCellFactory(new LineNumbersCellFactory<>());
            controller.colName.setCellValueFactory(new PropertyValueFactory<TreatmentLocation, String>("name"));
            controller.colCapacity.setCellValueFactory(new PropertyValueFactory<TreatmentLocation, String>("capacity"));
            controller.colCurrentRoom.setCellValueFactory(new PropertyValueFactory<TreatmentLocation, String>("currentRoom"));
            Callback<TableColumn<TreatmentLocation, String>, TableCell<TreatmentLocation, String>> cellFactory_1
                    =
                    new Callback<>() {
                        @Override
                        public TableCell call(final TableColumn<TreatmentLocation, String> param) {
                            final TableCell<TreatmentLocation, String> cell = new TableCell<>() {

                                final Button btn = new Button("Xóa địa điểm");

                                @Override
                                public void updateItem(String item, boolean empty) {
                                    super.updateItem(item, empty);
                                    if (empty) {
                                        setGraphic(null);
                                        setText(null);
                                    } else {
                                        btn.setOnAction(event -> {
                                            System.out.println(getClass() + "btn delete TreatmentLocation click");
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
            controller.colBtn.setCellFactory(cellFactory_1);


            for (String key : App.getInstance().getTreatmentLocationList().keySet()) {
                TreatmentLocation tloc = App.getInstance().getTreatmentLocationList().get(key);

                controller.tableViewTreatmentLocation.getItems().add(tloc);
            }

//
//            Callback<TableColumn<Patient, String>, TableCell<Patient, String>> cellPane
//                    =
//                    new Callback<>() {
//                        @Override
//                        public TableCell<Patient, String> call(final TableColumn<Patient, String> param) {
//                            final TableCell<Patient, String> cell = new TableCell<>() {
//
//                                final Pane pane = new Pane();
//
//                                @Override
//                                public void updateItem(String item, boolean empty) {
//                                    super.updateItem(item, empty);
//                                    if (empty) {
//                                        setGraphic(null);
//                                        setText(null);
//                                    } else {
//                                        getTableView().getItems().get(getIndex());
//                                        Patient p = getTableView().getItems().get(getIndex());
//
//                                        Label lbl = new Label(p.getStatusAsString());
//                                        // todo set size, color
//
//                                        Button btn = new Button("xem ds\nlien doi");
//                                        // todo set size, color
//
//                                        btn.setOnAction((event) -> {
//                                            //todo: show modal and handle data
//                                            ArrayList<Patient> cc = p.getCloseContacts();
//                                        });
//                                        pane.getChildren().addAll(lbl, btn);
//                                        // todo set size, color
//
//                                        setGraphic(pane);
//                                        setText(null);
//                                    }
//                                }
//                            };
//                            return cell;
//                        }
//                    };
//            controller.<your_column>.setCellFactory(cellPane)
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Manager
    public void gotoManagerHomePage() {
        try {
            replaceSceneContent("manager.homePage.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoManagePatientInfo() {
        try {
            replaceSceneContent("manager.managePatientInfo.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoManagerProducts() {
        try {
            replaceSceneContent("manager.manageProducts.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoPackageDetails(String packageID) {
        try {
            ViewPackageDetailsController ctrl = new ViewPackageDetailsController();
            ctrl.setPackageID(packageID);
            replaceSceneContent("manager.modalMngrPkgDetails.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoAddProdToPkg() {
        try {
            replaceSceneContent("manager.modalMngrAddProdToPkg.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoManagerPackages() {
        try {
            replaceSceneContent("manager.managePackages.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoAddPatient(){
        try {
            replaceSceneContent("manager.addPatient.fxml");
            AddPatientController ctrl = loader.getController();
            ctrl.stage = stage;
            ctrl.loader = loader;
            ctrl.f = 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Patient
    public void gotoPatientHomePage() {
        try {
            replaceSceneContent("patient.homePage.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoPatientInformation() {
        try {
            replaceSceneContent("patient.information.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoManagedHistory() {
        try {
            replaceSceneContent("patient.managedHistory.fxml");
            ManagedHistoryController controller = loader.getController();

            TableView tableView = controller.tableViewHistory;

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

            controller.colNO.setCellFactory(new LineNumbersCellFactory<>());
            controller.colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
            controller.colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
            controller.colContent.setCellValueFactory(new PropertyValueFactory<>("content"));

            for (String key : App.getInstance().getUserList().keySet()) {
                if (App.getInstance().getUserList().get(key).getRole() == IUser.Role.PATIENT) {
                    ArrayList<String> histories = App.getInstance().getUserList().get(key).getHistory().getHistory();
                    for (String history : histories) {
                        HistoryDetail historyDetail = new HistoryDetail(key,
                                history.split(";")[0],
                                history.split(";")[2],
                                history.split(";")[1]);
                        if (historyDetail.getType().equals("CHANGE_STATUS")) {
                            tableView.getItems().add(historyDetail);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoConsumptionHistory() {
        try {
            replaceSceneContent("patient.consumptionHistory.fxml");
            ConsumptionHistoryController controller = loader.getController();

            TableView<HistoryDetail> tableView = controller.tableViewHistory;

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

            controller.colNO.setCellFactory(new LineNumbersCellFactory<>());
            controller.colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
            controller.colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
            controller.colContent.setCellValueFactory(new PropertyValueFactory<>("content"));

            for (String key : App.getInstance().getUserList().keySet()) {
                if (App.getInstance().getUserList().get(key).getRole() == IUser.Role.PATIENT) {
                    ArrayList<String> histories = App.getInstance().getUserList().get(key).getHistory().getHistory();

                    for (String history : histories) {
                        HistoryDetail historyDetail = new HistoryDetail(key,
                                history.split(";")[0],
                                history.split(";")[2],
                                history.split(";")[1]);
                        if (historyDetail.getType().equals("BUY_PACKAGE")) {
                            tableView.getItems().add(historyDetail);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoPaymentHistory() {
        try {
            replaceSceneContent("patient.paymentHistory.fxml");
            PaymentHistoryController controller = loader.getController();

            TableView tableView = controller.tableViewHistory;

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

            controller.colNO.setCellFactory(new LineNumbersCellFactory<>());
            controller.colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
            controller.colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
            controller.colContent.setCellValueFactory(new PropertyValueFactory<>("content"));

            for (String key : App.getInstance().getUserList().keySet()) {
                if (App.getInstance().getUserList().get(key).getRole() == IUser.Role.PATIENT) {
                    ArrayList<String> histories = App.getInstance().getUserList().get(key).getHistory().getHistory();

                    for (String history : histories) {
                        HistoryDetail historyDetail = new HistoryDetail(key,
                                history.split(";")[0],
                                history.split(";")[2],
                                history.split(";")[1]);
                        if (historyDetail.getType().equals("PAYMENT")) {
                            tableView.getItems().add(historyDetail);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoBuyPackage() {
        try {
            replaceSceneContent("patient.buyPackage.fxml");
            BuyPackageController controller = loader.getController();

            GridPane gp = controller.gridPanePackage;

            int row = 0, col = 0;
            for (String key : App.getInstance().getProductManagement().getPackageList().keySet()) {
                Package pkg = App.getInstance().getProductManagement().getPackageList().get(key);

                if (pkg.getImg_src().contains("http")) {
                    PackagePane packagePane = new PackagePane(pkg);

                    Pane pTemp = packagePane.getPane();

                    GridPane.setConstraints(pTemp, col, row);
                    gp.getChildren().add(pTemp);
                    col += 1;
                    if (col == 4) {
                        col = 0;
                        row += 1;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    
    public void gotoPayPackage() {
        try {
            replaceSceneContent("patient.cart.fxml");
            CartController controller = loader.getController();

            GridPane gp = controller.gridPanePackage;

            int row = 0, col = 0;
            for (String key : App.getInstance().getProductManagement().getOrderList().keySet()) {
                Order order = App.getInstance().getProductManagement().getOrderList().get(key);

                HashMap<String, Pair<Package, Integer>> packageList = order.getPackageList();
                for (String key1 : packageList.keySet()) {
                    Package pkg = packageList.get(key1).getFirst();
                    Integer quantity = packageList.get(key1).getSecond();

                    if (pkg.getImg_src().contains("http")) {
                        PackagePane packagePane = new PackagePane(pkg);
                        packagePane.getTextQuantity().setText(String.valueOf(quantity));

                        Pane pTemp = packagePane.getPane();

                        GridPane.setConstraints(pTemp, col, row);
                        gp.getChildren().add(pTemp);
                        col += 1;
                        if (col == 4) {
                            col = 0;
                            row += 1;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoPayDebt() {
        try {
            replaceSceneContent("patient.payDebt.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void test() {
        try {
            replaceSceneContent("patient.cart.fxml");
            CartController controller = loader.getController();

            GridPane gp = controller.gridPanePackage;

            int row = 0, col = 0;

            for (String key : App.getInstance().getProductManagement().getOrderList().keySet()) {
                System.out.println(App.getInstance().getProductManagement().getOrderList().get(key).getPackageList());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Parent replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader(Index.class.getResource(fxml), null, new JavaFXBuilderFactory());
        Parent page = loader.load();

        getInstance().loader = loader;

        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(page, 1024, 768);
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(page);
        }
        stage.setTitle("Quản lý thông tin covid-19");
        stage.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649057250/JAVA/Icon/covid-test-purple-128_za151v.png"));
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.sizeToScene();

        return page;
    }
}
