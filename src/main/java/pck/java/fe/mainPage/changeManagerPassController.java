package pck.java.fe.mainPage;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class changeManagerPassController {
    public PasswordField currentPassword;
    public PasswordField newPassword;
    public PasswordField confirmPassword;
    public Button btnSignIn;
    public Label invalidDetails;

    protected
    String successMessage = String.format("-fx-text-fill: GREEN;");
    String errorMessage = String.format("-fx-text-fill: RED;");
    String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
    String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");

    public void onBtnSignInClick(ActionEvent ae) throws InterruptedException {
        if (ae.getSource() == btnSignIn) {
            // In case all the Password fields are left blank then display the error message
            if (currentPassword.getText().isBlank() || newPassword.getText().isBlank() || confirmPassword.getText().isBlank()) {
                invalidDetails.setStyle(errorMessage);
                invalidDetails.setText("Mật khẩu không được bỏ trống!");

                // When the current password are blank
                if (currentPassword.getText().isBlank()) {
                    currentPassword.setStyle(errorStyle);
                    new animatefx.animation.Shake(currentPassword).play();
                }

                // When the new password are blank
                if (newPassword.getText().isBlank()) {
                    newPassword.setStyle(errorStyle);
                    new animatefx.animation.Shake(newPassword).play();
                }

                // When the new password are blank
                if (confirmPassword.getText().isBlank()) {
                    confirmPassword.setStyle(errorStyle);
                    new animatefx.animation.Shake(confirmPassword).play();
                }
            } else {
                // Check if password is less than four characters, if so display error message
                if (newPassword.getText().length() < 4) {
                    invalidDetails.setText("Mật khẩu phải dài hơn 4 kí tự!");
                    invalidDetails.setStyle(errorMessage);
                    newPassword.setStyle(errorStyle);
                    new animatefx.animation.Shake(newPassword).play();
                } else if (!confirmPassword.getText().equals(newPassword.getText())) {
                    // Check if confirm password is equal to password
                    invalidDetails.setText("Mật khẩu không khớp!");
                    invalidDetails.setStyle(errorMessage);
                    confirmPassword.setStyle(errorStyle);
                    new animatefx.animation.Shake(confirmPassword).play();
                }
                // If all change password details are entered as required then display success message
                else {
                    invalidDetails.setStyle(successMessage);
                    currentPassword.setStyle(successStyle);
                    newPassword.setStyle(successStyle);
                    confirmPassword.setStyle(successStyle);

                    invalidDetails.setText("Đổi mật khẩu thành công.");
                }
            }
        }
    }
}

