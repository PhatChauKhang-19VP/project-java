package pck.java.fe.mainPage;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class createAdminAccController {
    public TextField usernameTextField;
    public PasswordField userPassword;
    public PasswordField userPassword1;
    public Button btnSignIn;
    public Label invalidDetails;

    protected
    String successMessage = String.format("-fx-text-fill: GREEN;");
    String errorMessage = String.format("-fx-text-fill: RED;");
    String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
    String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");

    public void onBtnSignInClick(ActionEvent ae) throws InterruptedException {

        if (ae.getSource() == btnSignIn) {
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

                    invalidDetails.setText("Đăng nhập thành công.");
                }
            }
        }
    }
}
