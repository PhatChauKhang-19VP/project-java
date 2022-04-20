package pck.java;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.kordamp.bootstrapfx.BootstrapFX;
import pck.java.be.app.App;
import pck.java.database.DatabaseCommunication;
import pck.java.be.app.product.Package;
import pck.java.be.app.product.Product;
import pck.java.be.app.user.IUser;
import pck.java.be.app.user.Manager;
import pck.java.be.app.user.Patient;
import pck.java.be.app.util.TreatmentLocation;
import pck.java.fe.utils.LineNumbersCellFactory;
import pck.java.fe.utils.PackagePane;
import pck.java.fe.utils.PackagePaneManager;
import pck.java.fe.utils.ProductPane;

import java.io.IOException;

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

            //gotoSignIn();
            //gotoAdminHomePage();
            //gotoManagerHomePage();
            gotoPatientHomePage();

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

    public void gotoSignIn() {
        try {
            replaceSceneContent("mainPage.loginPage.fxml");
            pck.java.fe.mainPage.loginPageController controller = loader.getController();

//            for(String key : App.getInstance().getUserList().keySet()) {
//                String username = String.valueOf(controller.usernameTextField);
//                if(key.equals(username)) {
//                    if(App.getInstance().getUserList().get(key).getRole() == IUser.Role.PATIENT) {
//                        if(controller.getInvalidDetails().equals("Đăng nhập thành công.")) {
//                            gotoPatientHomePage();
//                        }
//                    }
//                    else if(App.getInstance().getUserList().get(key).getRole() == IUser.Role.ADMIN) {
//                        if(controller.getInvalidDetails().equals("Đăng nhập thành công.")) {
//                            gotoAdminHomePage();
//                        }
//                    }
//                    else if(App.getInstance().getUserList().get(key).getRole() == IUser.Role.MANAGER) {
//                        if(controller.getInvalidDetails().equals("Đăng nhập thành công.")) {
//                            gotoManagerHomePage();
//                        }
//                    }
//                }
//            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoAdminHomePage() {
        try {
            replaceSceneContent("admin.homePage.fxml");
            pck.java.fe.admin.HomePageController controller = loader.getController();

            // manager list
            controller.colNO.setCellFactory(new LineNumbersCellFactory<>());
            controller.colUsername.setCellValueFactory(new PropertyValueFactory<Manager, String>("username"));
            controller.colName.setCellValueFactory(new PropertyValueFactory<Manager, String>("name"));
            Callback<TableColumn<Manager, String>, TableCell<Manager, String>> cellFactory
                    =
                    new Callback<>() {
                        @Override
                        public TableCell call(final TableColumn<Manager, String> param) {
                            final TableCell<Manager, String> cell = new TableCell<Manager, String>() {

                                final Button btn = new Button("Khoá tài khoản");

                                @Override
                                public void updateItem(String item, boolean empty) {
                                    super.updateItem(item, empty);
                                    if (empty) {
                                        setGraphic(null);
                                        setText(null);
                                    } else {
                                        btn.setOnAction(event -> {
                                            System.out.println(getClass() + "btn ban manager click");
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
            controller.colBtn.setCellFactory(cellFactory);
            //DatabaseCommunication.getInstance().loadManagers();

            for (String key: App.getInstance().getUserList().keySet()){
                IUser iu = App.getInstance().getUserList().get(key);

                if (iu.getRole() == IUser.Role.MANAGER){
                    controller.tableViewManager.getItems().add((Manager) iu);
                }
            }

            // treatment location

            //databaseCommunication.getInstance().loadTreatmentLocations();
            controller.colNO_1.setCellFactory(new LineNumbersCellFactory<>());
            controller.colName_1.setCellValueFactory(new PropertyValueFactory<TreatmentLocation, String>("name"));
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
            controller.colBtn_1.setCellFactory(cellFactory_1);
            
            
            for (String key : App.getInstance().getTreatmentLocationList().keySet()){
                TreatmentLocation tloc = App.getInstance().getTreatmentLocationList().get(key);

                controller.tableViewTreatmentLocation.getItems().add(tloc);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoManagerHomePage() {
        try {
            replaceSceneContent("manager.homePage.fxml");
            pck.java.fe.manager.HomePageController controller = loader.getController();

            DatabaseCommunication.getInstance().loadPatients();

            TableView tableView = controller.tableViewPatient;

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
            controller.colName.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
            controller.colUsername.setCellValueFactory(new PropertyValueFactory<Patient, String>("username"));
            controller.colDob.setCellValueFactory(new PropertyValueFactory<Patient, String>("dobAsString"));
            controller.colAddress.setCellValueFactory(new PropertyValueFactory<Patient, String>("addressAsString"));
            controller.colStatus.setCellValueFactory(new PropertyValueFactory<Patient, String>("statusAsString"));
            controller.colTLoc.setCellValueFactory(new PropertyValueFactory<Patient, String>("treatmentLocationAsString"));

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
            controller.colBtnMod.setCellFactory(cellFactoryMod);

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
            controller.colBtnDel.setCellFactory(cellFactoryDel);

            for(String key : App.getInstance().getUserList().keySet()) {
                if(App.getInstance().getUserList().get(key).getRole() == IUser.Role.PATIENT) {
                    tableView.getItems().add(App.getInstance().getUserList().get(key));
                }
            }

            GridPane gp = controller.gridPaneProduct;

            int row = 0, col = 0;
            for (String key : App.getInstance().getProductManagement().getProductList().keySet()) {
                Product product = App.getInstance().getProductManagement().getProductList().get(key);

                if (product.getImgSrc().contains("http")) {
                    ProductPane productPane = new ProductPane(product);

                    Pane pTemp = productPane.getPane();

                    GridPane.setConstraints(pTemp, col, row);
                    gp.getChildren().add(pTemp);
                    col += 1;
                    if (col == 4) {
                        col = 0;
                        row += 1;
                    }
                }
            }

            gp = controller.gridPanePackage;

            row = 0;
            col = 0;
            for (String key : App.getInstance().getProductManagement().getPackageList().keySet()) {
                Package pkg = App.getInstance().getProductManagement().getPackageList().get(key);

                if (pkg.getImg_src().contains("http")) {
                    PackagePaneManager packagePane = new PackagePaneManager(pkg);

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

            controller.btnAddProd.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        System.out.println(getClass() + "btn add prod clicked");
                        Stage modalAddProd = new Stage();
                        FXMLLoader loader = new FXMLLoader(Index.class.getResource("manager.modalMngrAddProd.fxml"), null, new JavaFXBuilderFactory());
                        Parent root = loader.load();
                        modalAddProd.initOwner(getInstance().stage);
                        modalAddProd.setScene(new Scene(root));
                        modalAddProd.setTitle("Thêm nhu yếu phẩm");
                        modalAddProd.initModality(Modality.APPLICATION_MODAL);

                        modalAddProd.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
                        modalAddProd.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                        modalAddProd.setResizable(false);
                        modalAddProd.setFullScreen(false);
                        modalAddProd.sizeToScene();
                        modalAddProd.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            controller.btnAddPkg.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        System.out.println(getClass() + "btn add pkg clicked");
                        Stage modalAddProd = new Stage();
                        FXMLLoader loader = new FXMLLoader(Index.class.getResource("manager.modalMngrAddPkg.fxml"), null, new JavaFXBuilderFactory());
                        Parent root = loader.load();
                        modalAddProd.initOwner(getInstance().stage);
                        modalAddProd.setScene(new Scene(root));
                        modalAddProd.setTitle("Thêm gói nhu yếu phẩm");
                        modalAddProd.initModality(Modality.APPLICATION_MODAL);

                        modalAddProd.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
                        modalAddProd.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                        modalAddProd.setResizable(false);
                        modalAddProd.setFullScreen(false);
                        modalAddProd.sizeToScene();
                        modalAddProd.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            controller.btnDEBUGmodPkg.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        System.out.println(getClass() + "btn mod pkg clicked");
                        Stage modalAddProd = new Stage();
                        FXMLLoader loader = new FXMLLoader(Index.class.getResource("manager.modalMngrModPkg.fxml"), null, new JavaFXBuilderFactory());
                        Parent root = loader.load();
                        modalAddProd.initOwner(getInstance().stage);
                        modalAddProd.setScene(new Scene(root));
                        modalAddProd.setTitle("Chỉnh sửa gói nhu yếu phẩm");
                        modalAddProd.initModality(Modality.APPLICATION_MODAL);

                        modalAddProd.getIcons().add(new Image("https://res.cloudinary.com/phatchaukhang/image/upload/v1649255070/HQTCSDL/Icon/icon-shop_d9bmh0.png"));
                        modalAddProd.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                        modalAddProd.setResizable(false);
                        modalAddProd.setFullScreen(false);
                        modalAddProd.sizeToScene();
                        modalAddProd.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoPatientHomePage() throws Exception {
        try {
            replaceSceneContent("patient.homePage.fxml");
            pck.java.fe.patient.HomePageController controller = loader.getController();

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
        stage.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.sizeToScene();

        return page;
    }
}
