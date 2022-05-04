package pck.java.fe.mainPage;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import pck.java.Index;
import pck.java.be.app.App;
import pck.java.be.app.user.*;
import pck.java.database.DatabaseCommunication;
import pck.java.database.SelectQuery;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class loginPageController {
    public TextField usernameTextField;
    public PasswordField userPassword;
    public Button btnSignIn;
    public Label invalidDetails;
    public Label invalidDetails1;
    public Button btnContinue;
    public Pane passwordPane;

    protected
    String successMessage = String.format("-fx-text-fill: GREEN;");
    String errorMessage = String.format("-fx-text-fill: RED;");
    String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
    String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");

    public void checkUsername() throws Exception {
        String username = usernameTextField.getText();

        for (String key : App.getInstance().getUserList().keySet()) {
            IUser iu = App.getInstance().getUserList().get(key);

            String temp = iu.getUsername();

            if (Objects.equals(temp, username)) {
                if (iu.getRole() == IUser.Role.ADMIN) {
                    pck.java.Index.getInstance().gotoAdminHomePage();
                } else if (iu.getRole() == IUser.Role.MANAGER) {
                    pck.java.Index.getInstance().gotoManagerHomePage();
                } else if (iu.getRole() == IUser.Role.PATIENT) {
                    pck.java.Index.getInstance().gotoPatientHomePage();
                }
                break;
            } else {
                invalidDetails.setText("Tên đăng nhập không tồn tại!");
                invalidDetails.setStyle(errorMessage);
                usernameTextField.setStyle(errorStyle);
                usernameTextField.setStyle(errorStyle);
            }
        }
    }

    public void onBtnContinueClick(ActionEvent ae) throws InterruptedException {
        if (ae.getSource() == btnContinue) {
            if (usernameTextField.getText().isBlank()) {
                invalidDetails1.setText("Tên tài khoản không được bỏ trống!");
                usernameTextField.setStyle(errorStyle);
                invalidDetails1.setStyle(errorMessage);
                new animatefx.animation.Shake(usernameTextField).play();
            } else {
                DatabaseCommunication dbc = DatabaseCommunication.getInstance();
                SelectQuery sq = new SelectQuery();

                sq.select("*").from("LOGIN_INFOS")
                        .where("username='" + usernameTextField.getText() + "'")
                        .where("user_type='MANAGER'")
                        .where("password is NULL");

                try {
                    List<Map<String, Object>> rs = dbc.executeQuery(sq.getQuery());
                    if (rs.size() != 0)
                        Index.getInstance().gotoChangeManagerPassword(usernameTextField.getText());
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                btnContinue.setVisible(false);
                invalidDetails1.setVisible(false);
                passwordPane.setVisible(true);
                btnSignIn.setVisible(true);
            }
        }
    }

    private boolean signIn(String username, String password) {
        try {
            DatabaseCommunication dbc = DatabaseCommunication.getInstance();
            SelectQuery sq = new SelectQuery();
            sq.select("*").from("LOGIN_INFOS").where("username", "'" + username + "'")
                    .where("password", "HASHBYTES('SHA2_512', '" + password + "')").where("account_status='ACTIVE'");

            System.out.println(sq.getQuery());

            List<Map<String, Object>> rs = dbc.executeQuery(sq.getQuery());
            if (rs.size() == 0) {
                return false;
            }

            Map<String, Object> map = rs.get(0);
            switch (String.valueOf(map.get("user_type"))) {
                case "ADMIN":
                    Admin admin = Admin.getInstance();
                    admin.setUsername(username);
                    App.getInstance().setCurrentUser(admin);
                    break;
                case "MANAGER":
                    Manager mng = new Manager(new UserConcreteComponent());
                    mng.setUsername(username);
                    mng.setRole(IUser.Role.MANAGER);
                    App.getInstance().setCurrentUser(mng);
                    break;
                case "PATIENT":
                    Patient pt = new Patient(new UserConcreteComponent());
                    pt.setUsername(username);
                    pt.setRole(IUser.Role.PATIENT);
                    App.getInstance().setCurrentUser(pt);
                    break;
                default:
                    return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void onBtnSignInClick(ActionEvent ae) throws Exception {
        if (ae.getSource() == btnSignIn) {
            // In case the Username and Password fields are left blank then display the error message
            if (usernameTextField.getText().isBlank() || userPassword.getText().isBlank()) {
                invalidDetails.setStyle(errorMessage);

                // When the username and password are blank
                if (usernameTextField.getText().isBlank() && userPassword.getText().isBlank()) {
                    invalidDetails.setText("Tên tài khoản và mật khẩu không được bỏ trống!");
                    usernameTextField.setStyle(errorStyle);
                    userPassword.setStyle(errorStyle);
                    new animatefx.animation.Shake(usernameTextField).play();
                    new animatefx.animation.Shake(userPassword).play();

                } else {
                    // When only the username is blank
                    if (usernameTextField.getText().isBlank()) {
                        usernameTextField.setStyle(errorStyle);
                        invalidDetails.setText("Tên tài khoản không được bỏ trống!");
                        userPassword.setStyle(successStyle);
                        new animatefx.animation.Shake(usernameTextField).play();
                    } else {
                        // When only the password is blank
                        if (userPassword.getText().isBlank()) {
                            userPassword.setStyle(errorStyle);
                            invalidDetails.setText("Mật khẩu không được bỏ trống!");
                            usernameTextField.setStyle(successStyle);
                            new animatefx.animation.Shake(userPassword).play();
                        }
                    }
                }
            } else {
                // Check if password is less than four characters, if so display error message
                if (userPassword.getText().length() < 4) {
                    invalidDetails.setText("Mật khẩu phải dài hơn 4 kí tự!");
                    invalidDetails.setStyle(errorMessage);
                    userPassword.setStyle(errorStyle);
                    new animatefx.animation.Shake(userPassword).play();
                }
                // If all login details are entered as required then display success message
                else {
                    boolean signInRes = signIn(usernameTextField.getText(), userPassword.getText());
                    if (!signInRes) {
                        invalidDetails.setText("Tài khoản hoặc mật khẩu không đúng!");
                        invalidDetails.setStyle(errorMessage);
                        usernameTextField.setStyle(errorStyle);
                        userPassword.setStyle(errorStyle);
                        new animatefx.animation.Shake(usernameTextField).play();
                        new animatefx.animation.Shake(userPassword).play();
                    } else {
                        invalidDetails.setStyle(successMessage);
                        invalidDetails.setText("Đăng nhập thành công!");
                        usernameTextField.setStyle(successStyle);
                        userPassword.setStyle(successStyle);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thông báo");
                        alert.setHeaderText("Đăng nhập thành công!");

                        Optional<ButtonType> option = alert.showAndWait();

                        if (option.isPresent() && option.get() == ButtonType.OK) {
                            IUser iu = App.getInstance().getCurrentUser();
                            if (iu.getRole() == IUser.Role.ADMIN) {
                                pck.java.Index.getInstance().gotoAdminHomePage();
                            } else if (iu.getRole() == IUser.Role.MANAGER) {
                                pck.java.Index.getInstance().gotoManagerHomePage();
                            } else if (iu.getRole() == IUser.Role.PATIENT) {
                                pck.java.Index.getInstance().gotoPatientHomePage();
                            }
                        }
                    }
                }
            }
        }
    }
}

