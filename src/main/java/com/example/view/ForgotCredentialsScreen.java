package com.example.view;

import java.util.HashMap;

import com.example.constroller.ScreenController;
import com.example.constroller.UserController;
import com.example.model.UserToken;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ForgotCredentialsScreen extends VBox {

    private void clearFields(PasswordField newPasswordField, PasswordField confirmPasswordField,
            PasswordField passwordField, TextField emailField) {

        if (passwordField != null) {
            passwordField.clear();
        }

        if (newPasswordField != null) {
            newPasswordField.clear();

        }

        if (confirmPasswordField != null) {
            confirmPasswordField.clear();
        }

        if (emailField != null) {
            emailField.clear();
        }

    }

    private VBox createVbox(UserToken userToken, Label labelError, Label emailLabel, TextField emailField,
            Label emailError, Label passwordLabel, PasswordField passwordField, Label passwordError,
            Label newPasswordLabel, PasswordField newPasswordField, Label newPasswordError, Label confirmPasswordLabel,
            PasswordField confirmPasswordField, Label confirmPasswordError, Button resetButton, Button backButton) {
        return userToken != null
                ? new VBox(5, labelError, newPasswordLabel, newPasswordField, newPasswordError, confirmPasswordLabel,
                        confirmPasswordField, confirmPasswordError, resetButton, backButton)
                : new VBox(5, labelError, emailLabel, emailField, emailError, passwordLabel, passwordField,
                        passwordError, newPasswordLabel, newPasswordField, newPasswordError, confirmPasswordLabel,
                        confirmPasswordField, confirmPasswordError, resetButton, backButton);
    }

    private void resetButtonAction(Stage stage, TextField emailField, PasswordField passwordField,
            PasswordField newPasswordField, PasswordField confirmPasswordField, Label emailError, Label passwordError,
            Label newPasswordError, Label confirmPasswordError, UserController userController,
            ScreenController screenController, Label labelError, HashMap<String, String> errors, UserToken userToken) {

        boolean valid = true;

        if (userToken == null) {
            if (emailField.getText().isBlank()) {
                emailError.setText("Email is required");
                valid = false;
            }

            if (passwordField.getText().isBlank()) {
                passwordError.setText("Current password is required");
                valid = false;
            }
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
            if (userToken != null) {
                boolean updateSuccess = userController.updateLoggedInAccount(newPasswordField.getText(),
                        confirmPasswordField.getText());
                if (updateSuccess) {
                    clearFields(newPasswordField, confirmPasswordField, null, null);
                    screenController.activate("login", stage);
                } else {
                    labelError.setText("Password update failed, user might not be logged in.");
                }
            } else {
                boolean updateSuccess = userController.updateNotLoggedAccount(emailField.getText(),
                        passwordField.getText(), newPasswordField.getText(), confirmPasswordField.getText());
                if (updateSuccess) {
                    clearFields(newPasswordField, confirmPasswordField, passwordField, emailField);
                    screenController.activate("login", stage);
                } else {
                    labelError.setText("Password update failed, account not found or credentials incorrect.");
                }
            }
        }
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

    public ForgotCredentialsScreen(Stage stage, ScreenController screenController, UserController userController) {

        HashMap<String, String> errors = userController.getInputErrors();
        UserToken userToken = userController.getLoggedUser();

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
            resetButtonAction(stage, emailField, passwordField, newPasswordField, confirmPasswordField, emailError,
                    passwordError, newPasswordError, confirmPasswordError, userController, screenController, labelError,
                    errors, userToken);
        });

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("button");
        backButton.setOnAction(event -> {
            screenController.activate("login", stage);
        });

        VBox form = createVbox(userToken, labelError, emailLabel, emailField, emailError, passwordLabel, passwordField,
                passwordError, newPasswordLabel, newPasswordField, newPasswordError, confirmPasswordLabel,
                confirmPasswordField, confirmPasswordError, resetButton, backButton);

        form.setAlignment(Pos.CENTER);

        this.getChildren().add(form);
        this.setAlignment(Pos.CENTER);
    }

    public static void show(Stage primaryStage, ScreenController screenController, UserController userController) {
        Scene scene = new Scene(new ForgotCredentialsScreen(primaryStage, screenController, userController));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
