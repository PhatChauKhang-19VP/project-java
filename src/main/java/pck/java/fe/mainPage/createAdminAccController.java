package pck.java.fe.mainPage;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import pck.java.Index;
import pck.java.database.DatabaseCommunication;
import pck.java.database.InsertQuery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class createAdminAccController {
    public TextField usernameTextField;
    public PasswordField userPassword;
    public PasswordField userPassword1;
    public Button btnSignUp;
    public Label invalidDetails;

    protected
    String successMessage = String.format("-fx-text-fill: GREEN;");
    String errorMessage = String.format("-fx-text-fill: RED;");
    String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
    String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");

    public void onBtnSignInClick(ActionEvent ae) throws InterruptedException {

        if (ae.getSource() == btnSignUp) {
            // In case the Username and Password fields are left blank then display the error message
            if (usernameTextField.getText().isBlank() || userPassword.getText().isBlank() || userPassword1.getText().isBlank()) {
                invalidDetails.setStyle(errorMessage);

                // When the username, password and confirm password are blank
                if (usernameTextField.getText().isBlank() && userPassword.getText().isBlank() && userPassword1.getText().isBlank()) {
                    invalidDetails.setText("Tên tài khoản và mật khẩu không được bỏ trống!");
                    usernameTextField.setStyle(errorStyle);
                    userPassword.setStyle(errorStyle);
                    userPassword1.setStyle(errorStyle);
                    new animatefx.animation.Shake(usernameTextField).play();
                    new animatefx.animation.Shake(userPassword).play();
                    new animatefx.animation.Shake(userPassword1).play();
                } else {
                    // When only the username is blank
                    if (usernameTextField.getText().isBlank()) {
                        usernameTextField.setStyle(errorStyle);
                        invalidDetails.setText("Tên tài khoản không được bỏ trống!");
                        userPassword.setStyle(successStyle);
                        new animatefx.animation.Shake(usernameTextField).play();
                    } else if (userPassword.getText().isBlank()) {
                        // When only the password is blank
                        userPassword.setStyle(errorStyle);
                        invalidDetails.setText("Mật khẩu không được bỏ trống!");
                        usernameTextField.setStyle(successStyle);
                        new animatefx.animation.Shake(userPassword).play();
                    } else if (userPassword1.getText().isBlank()) {
                        // When only the confirm password is blank
                        userPassword1.setStyle(errorStyle);
                        invalidDetails.setText("Mật khẩu không được bỏ trống!");
                        usernameTextField.setStyle(successStyle);
                        new animatefx.animation.Shake(userPassword1).play();
                    }
                }
            } else {
                // Check if password is less than four characters, if so display error message
                if (userPassword.getText().length() < 4) {
                    invalidDetails.setText("Mật khẩu phải dài hơn 4 kí tự!");
                    invalidDetails.setStyle(errorMessage);
                    userPassword.setStyle(errorStyle);
                    new animatefx.animation.Shake(userPassword).play();
                } else if (!userPassword.getText().equals(userPassword1.getText())) {
                    invalidDetails.setText("Mật khẩu không khớp!");
                    invalidDetails.setStyle(errorMessage);
                    userPassword1.setStyle(errorStyle);
                    new animatefx.animation.Shake(userPassword1).play();
                } else {
                    invalidDetails.setStyle(successMessage);
                    usernameTextField.setStyle(successStyle);
                    userPassword.setStyle(successStyle);
                    userPassword1.setStyle(successStyle);

                    DatabaseCommunication dbc = DatabaseCommunication.getInstance();
                    InsertQuery iq = new InsertQuery();
                    ArrayList<String> cols = new ArrayList<>() {
                        {
                            add("username");
                            add("password");
                            add("account_status");
                            add("user_type");
                        }
                    };
                    ArrayList<String> vals = new ArrayList<>() {
                        {
                            add("'" + usernameTextField.getText() + "'");
                            add("HASHBYTES('SHA2_512', '" + userPassword.getText() + "')");
                            add("'ACTIVE'");
                            add("'ADMIN'");
                        }
                    };
                    iq.insertInto("LOGIN_INFOS").columns(cols).values(vals);

                    try {
                        dbc.execute(iq.getQuery());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    iq.clear();
                    cols = new ArrayList<>() {
                        {
                            add("username");
                            add("name");
                        }
                    };
                    vals = new ArrayList<>() {
                        {
                            add("'" + usernameTextField.getText() + "'");
                            add("'" + usernameTextField.getText() + "'");
                        }
                    };
                    iq.insertInto("ADMINS").columns(cols).values(vals);

                    try {
                        dbc.execute(iq.getQuery());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Đăng ký tài khoản Admin");
                    alert.setHeaderText("Bạn đã đăng ký thành công!");

                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.isPresent() && option.get() == ButtonType.OK) {
                        Index.getInstance().gotoAdminHomePage();
                    }
                }
            }
        }
    }
}
