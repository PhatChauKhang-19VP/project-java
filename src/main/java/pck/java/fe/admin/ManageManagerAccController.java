package pck.java.fe.admin;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import pck.java.Index;
import pck.java.be.app.App;
import pck.java.be.app.user.IUser;
import pck.java.be.app.user.Manager;
import pck.java.database.DatabaseCommunication;
import pck.java.fe.utils.LineNumbersCellFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageManagerAccController implements Initializable {
    public Button logoutButton;
    public Button backButton;
    public TableView tableViewManager;
    public TableColumn colNO;
    public TableColumn colUsername;
    public TableColumn colName;
    public TableColumn colBtn;
    public Button addMngButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DatabaseCommunication.getInstance().loadManagers();
        colNO.setCellFactory(new LineNumbersCellFactory<>());
        colUsername.setCellValueFactory(new PropertyValueFactory<Manager, String>("username"));
        colName.setCellValueFactory(new PropertyValueFactory<Manager, String>("name"));
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
        colBtn.setCellFactory(cellFactory);
        //DatabaseCommunication.getInstance().loadManagers();

        for (String key : App.getInstance().getUserList().keySet()) {
            IUser iu = App.getInstance().getUserList().get(key);

            if (iu.getRole() == IUser.Role.MANAGER) {
                tableViewManager.getItems().add((Manager) iu);
            }
        }
    }

    public void onBackButtonCliked(ActionEvent actionEvent) throws Exception {
        if(actionEvent.getSource() == backButton) {
            Index.getInstance().gotoAdminHomePage();
        }
    }

    public void onLogoutButtonCliked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == logoutButton) {
            pck.java.Index.getInstance().gotoSignIn();
        }
    }

    public void onAddManagerButtonCliked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == addMngButton) {
            pck.java.Index.getInstance().gotoCreateManagerAccount();
        }
    }
}
