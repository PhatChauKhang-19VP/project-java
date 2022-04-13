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
import pck.java.fe.patient.HomePageController;
import pck.java.fe.utils.LineNumbersCellFactory;
import pck.java.fe.utils.PackagePane;
import pck.java.fe.utils.ProductPane;

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
            gotoPatientHomePage();


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

            DatabaseCommunication.getInstance().loadManagers();

            TableView tableView = controller.tableViewManager;
            controller.colNO.setCellFactory(new LineNumbersCellFactory<>());
            controller.colName.setCellValueFactory(new PropertyValueFactory<Manager, String>("username"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoManagerHomePage() {

    }

    public void gotoPatientHomePage() throws Exception {
        replaceSceneContent("patient.homePage.fxml");
        pck.java.fe.patient.HomePageController controller = loader.getController();

        GridPane gp = controller.getGridPaneProduct();

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
        //stage.getScene().getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.sizeToScene();

        return page;
    }
}
