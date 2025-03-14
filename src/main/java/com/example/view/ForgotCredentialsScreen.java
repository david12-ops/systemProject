package com.example.view;

import java.util.HashMap;

import com.example.constroller.ScreenController;
import com.example.constroller.UserController;
import com.example.model.User;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ForgotCredentialsScreen extends VBox {

    private void resetButtonAction(TextField emailField, PasswordField passwordField, PasswordField newPasswordField,
            PasswordField confirmPasswordField, Label emailError, Label passwordError, Label newPasswordError,
            Label confirmPasswordError, UserController userController, ScreenController screenController,
            Label labelError, HashMap<String, String> errors, User loggedUser) {

        boolean valid = true;

        if (emailField.getText().isBlank()) {
            emailError.setText("Email is required");
            valid = false;
        }

        if (passwordField.getText().isBlank()) {
            passwordError.setText("Password is required");
            valid = false;
        }

        if (newPasswordField.getText().isBlank()) {
            newPasswordError.setText("New password is required");
            valid = false;
        } else {
            if (errors.get("password") != null) {
                newPasswordError.setText(errors.get("password"));
                valid = false;
            }
        }

        if (confirmPasswordField.getText().isBlank()) {
            confirmPasswordError.setText("Confirmation password is required");
            valid = false;
        } else {
            if (errors.get("confirmPassword") != null) {
                confirmPasswordError.setText(errors.get("confirmPassword"));
                valid = false;
            }
        }

        if (valid) {
            if (loggedUser != null) {

                if (userController.updateLoggedInAccount(newPasswordField.getText(), confirmPasswordField.getText())) {
                    screenController.activate("login");
                } else {
                    labelError.setText("Password update failed, user was not logged or something failed");
                }
            } else {

                if (userController.updateNotLoggedAccount(emailField.getText(), passwordField.getText(),
                        newPasswordField.getText(), confirmPasswordField.getText())) {

                    screenController.activate("login");
                } else {
                    labelError.setText("Password update failed, user was not found or something failed");
                }

            }
        }
    }

    private VBox createForLogInUserVbox(Label labelError, Label newPasswordLabel, PasswordField newPasswordField,
            Label newPasswordError, Label confirmPasswordLabel, PasswordField confirmPasswordField,
            Label confirmPasswordError, Button resetButton, Button backButton) {
        return new VBox(5, labelError, newPasswordLabel, newPasswordField, newPasswordError, confirmPasswordLabel,
                confirmPasswordField, confirmPasswordError, resetButton, backButton);
    }

    private void onchangeInitialize(TextField emailField, PasswordField passwordField, PasswordField newPasswordField,
            PasswordField confirmPasswordField, Label emailError, Label passwordError, Label newPasswordError,
            Label confirmPasswordError, Label labelError, HashMap<String, String> errors) {
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                labelError.setText("");
                emailError.setText("");
            }
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                labelError.setText("");
                passwordError.setText("");
            }
        });

        newPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                newPasswordError.setText("");
                labelError.setText("");
                errors.remove("newPassword");
                errors.remove("confirmPassword");
            }
        });

        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                confirmPasswordError.setText("");
                labelError.setText("");
                errors.remove("confirmPassword");
            }
        });
    }

    public ForgotCredentialsScreen(Stage stage, ScreenController screenController, UserController userController,
            User loggedUser) {

        HashMap<String, String> errors = userController.getInputErrors();

        Label labelError = new Label();
        labelError.getStyleClass().add("error-label");

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label emailError = new Label();
        emailError.getStyleClass().add("error-label");

        Label passwordLabel = new Label("Current password:");
        PasswordField passwordField = new PasswordField();
        Label passwordError = new Label();
        passwordError.getStyleClass().add("error-label");

        Label newPasswordLabel = new Label("New password:");
        PasswordField newPasswordField = new PasswordField();
        Label newPasswordError = new Label();
        newPasswordError.getStyleClass().add("error-label");

        Label confirmPasswordLabel = new Label("Confirm new password:");
        PasswordField confirmPasswordField = new PasswordField();
        Label confirmPasswordError = new Label();
        confirmPasswordError.getStyleClass().add("error-label");

        onchangeInitialize(emailField, passwordField, newPasswordField, confirmPasswordField, emailError, passwordError,
                newPasswordError, confirmPasswordError, labelError, errors);

        Button resetButton = new Button("Reset");
        resetButton.getStyleClass().add("button");
        resetButton.setOnAction(event -> {
            resetButtonAction(emailField, passwordField, newPasswordField, confirmPasswordField, emailError,
                    passwordError, newPasswordError, confirmPasswordError, userController, screenController, labelError,
                    errors, loggedUser);
        });

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("button");
        backButton.setOnAction(event -> {
            screenController.activate("login");
        });

        VBox form = loggedUser != null
                ? createForLogInUserVbox(labelError, newPasswordLabel, newPasswordField, newPasswordError,
                        confirmPasswordLabel, confirmPasswordField, confirmPasswordError, resetButton, backButton)
                : new VBox(5, labelError, emailLabel, emailField, emailError, passwordLabel, passwordField,
                        passwordError, newPasswordLabel, newPasswordField, newPasswordError, confirmPasswordLabel,
                        confirmPasswordField, confirmPasswordError, resetButton, backButton);
        form.setAlignment(Pos.CENTER);

        this.getChildren().add(form);
        this.setAlignment(Pos.CENTER);
    }

    public static void show(Stage primaryStage, ScreenController screenController, UserController controller,
            User loggedUser) {
        Scene scene = new Scene(new ForgotCredentialsScreen(primaryStage, screenController, controller, loggedUser),
                300, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Reset password");
        primaryStage.show();
    }

}
