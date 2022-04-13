package pck.java.fe.mainPage;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import pck.java.be.app.App;

import static java.lang.Thread.sleep;

public class loginPageController {
    public TextField usernameTextField;
    public PasswordField userPassword;
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
                    String signInRes = "";

//                    if (signInRes.equals("LOGIN_FAILED")) {
//                        invalidDetails.setText("Đăng nhập thất bại.\nTài khoản hoặc mật khẩu không chính xác.");
//                        invalidDetails.setStyle(errorMessage);
//                        usernameTextField.setStyle(errorStyle);
//                        userPassword.setStyle(errorStyle);
//
//                        new animatefx.animation.Shake(usernameTextField).play();
//                        new animatefx.animation.Shake(userPassword).play();
//                    } else if (signInRes.equals("PENDING")) {
//                        invalidDetails.setText("Tài khoản chưa được kích hoạt, vui lòng thử lại sau.");
//                        invalidDetails.setStyle(errorMessage);
//                        usernameTextField.setStyle(errorStyle);
//                        userPassword.setStyle(errorStyle);
//
//                        new animatefx.animation.Shake(usernameTextField).play();
//                        new animatefx.animation.Shake(userPassword).play();
//                    } else if (signInRes.equals("ERROR")) {
//                        invalidDetails.setText("Có lỗi khi đăng nhập, vui lòng thử lại");
//                        invalidDetails.setStyle(errorMessage);
//                        usernameTextField.setStyle(errorStyle);
//                        userPassword.setStyle(errorStyle);
//
//                        new animatefx.animation.Shake(usernameTextField).play();
//                        new animatefx.animation.Shake(userPassword).play();
//                    } else {

                    invalidDetails.setStyle(successMessage);
                    usernameTextField.setStyle(successStyle);
                    userPassword.setStyle(successStyle);

                    new animatefx.animation.Shake(usernameTextField).play();
                    new animatefx.animation.Shake(userPassword).play();

                    invalidDetails.setText("Đăng nhập thành công.");

//
//                    Platform.runLater(new Thread(() -> {
//                        try {
//                            System.out.println("Sleep 1s");
//                            sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        //App.getInstance().gotoHome();
//                    }));
                }
            }
        }
    }
}

