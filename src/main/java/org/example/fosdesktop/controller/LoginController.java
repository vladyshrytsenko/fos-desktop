package org.example.fosdesktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.example.fosdesktop.service.AuthService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginController {

    @FXML
    private void onRegister() {
        String email = this.emailField.getText();
        String username = this.usernameField.getText();
        String password = this.passwordField.getText();
        String confirmPassword = this.confirmPasswordField.getText();

        if (username.isEmpty()
            || email.isEmpty()
            || password.isEmpty()
            || confirmPassword.isEmpty()) {

            this.showAlert(
                Alert.AlertType.ERROR,
                "Error",
                "All fields are required!"
            );
            return;
        }

        if (!password.equals(confirmPassword)) {
            this.showAlert(
                Alert.AlertType.ERROR,
                "Error",
                "Passwords do not match"
            );
            return;
        }

        boolean success = this.authService.register(username, email, password);

        if (success) {
            this.showAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                "Registration was successful"
            );
            this.authService.login();
        } else {
            this.showAlert(
                Alert.AlertType.ERROR,
                "Registration error",
                "An error occurred while registering"
            );
        }
    }

    @FXML
    private void onLogin() {
        this.authService.login();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private final AuthService authService;

    @FXML
    private TextField usernameField, emailField;

    @FXML
    private PasswordField passwordField, confirmPasswordField;

    @FXML
    private Button loginButton, registerButton;

}
