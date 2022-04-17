package pck.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.kordamp.bootstrapfx.BootstrapFX;
import pck.java.be.app.App;
import pck.java.be.app.database.DatabaseCommunication;
import pck.java.be.app.product.Package;
import pck.java.be.app.product.Product;
import pck.java.be.app.user.IUser;
import pck.java.be.app.user.Manager;
import pck.java.be.app.user.Patient;
import pck.java.be.app.util.TreatmentLocation;
import pck.java.fe.patient.HomePageController;
import pck.java.fe.utils.LineNumbersCellFactory;
import pck.java.fe.utils.PackagePane;
import pck.java.fe.utils.PackagePaneManager;
import pck.java.fe.utils.ProductPane;

import java.time.LocalDate;

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
            gotoManagerHomePage();
            //gotoPatientHomePage();

            primaryStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoSignIn() {
        try {
            replaceSceneContent("mainPage.loginPage.fxml");
                    } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoAdminHomePage() {
        try {
            replaceSceneContent("admin.homePage.fxml");
            pck.java.fe.admin.HomePageController controller = loader.getController();

            DatabaseCommunication.getInstance().loadTreatmentLocations();
            TableView tableView = controller.tableViewTreatmentLocation;
            controller.colNO.setCellFactory(new LineNumbersCellFactory<>());
            controller.colName_1.setCellValueFactory(new PropertyValueFactory<TreatmentLocation, String>("name"));
            controller.colCapacity.setCellValueFactory(new PropertyValueFactory<TreatmentLocation, String>("capacity"));
            controller.colCurrentRoom.setCellValueFactory(new PropertyValueFactory<TreatmentLocation, String>("currentRoom"));

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
            controller.colNO.setCellFactory(new LineNumbersCellFactory<>());
            controller.colName.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
            controller.colUsername.setCellValueFactory(new PropertyValueFactory<Patient, String>("username"));
            controller.colDob.setCellValueFactory(new PropertyValueFactory<Patient, String>("dobAsString"));
            controller.colAddress.setCellValueFactory(new PropertyValueFactory<Patient, String>("addressAsString"));
            controller.colStatus.setCellValueFactory(new PropertyValueFactory<Patient, String>("statusAsString"));
            controller.colTLoc.setCellValueFactory(new PropertyValueFactory<Patient, String>("treatmentLocationAsString"));

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
        stage.setTitle("Quản lý thông tin COVID-19");
        //stage.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.sizeToScene();

        return page;
    }
}
