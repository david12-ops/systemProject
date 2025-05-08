package com.example.view;

import com.example.controller.ScreenController;
import com.example.controller.UserController;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterScreen extends VBox {

    private void clearFields(Label emailErrorLabel, Label passwordErrorLabel, TextField emailField,
            PasswordField passwordField, PasswordField confirmPasswordField, Label confirmPasswordErrorLabel,
            UserController userController) {
        emailErrorLabel.setText("");
        passwordErrorLabel.setText("");
        emailField.clear();
        passwordField.clear();
        confirmPasswordErrorLabel.setText("");
        confirmPasswordField.setText("");
        userController.clearError("confirmPassword");
        userController.clearError("email");
        userController.clearError("password");
    }

    private void registerButtonAction(Stage stage, TextField emailField, PasswordField passwordField,
            PasswordField confirmPasswordField, Label confirmPasswordErrorLabel, Label emailErrorLabel,
            Label passwordErrorLabel, UserController userController, ScreenController screenController,
            Label labelError) {

        boolean isBlankField = false;
        boolean valid = true;

        if (emailField.getText().isBlank()) {
            emailErrorLabel.setText("Email is required");
            isBlankField = true;
            valid = false;
        }

        if (passwordField.getText().isBlank()) {
            passwordErrorLabel.setText("Password is required");
            isBlankField = true;
            valid = false;
        }

        if (confirmPasswordField.getText().isBlank()) {
            confirmPasswordErrorLabel.setText("Confirmation password is required");
            isBlankField = true;
            valid = false;
        }

        boolean registerSuccess = userController.register(emailField.getText(), passwordField.getText(),
                confirmPasswordField.getText());

        if (!isBlankField && userController.getError("password") != null) {
            passwordErrorLabel.setText(userController.getError("password"));
            isBlankField = false;
            valid = false;
        }

        if (!isBlankField && userController.getError("confirmPassword") != null) {
            confirmPasswordErrorLabel.setText(userController.getError("confirmPassword"));
            isBlankField = false;
            valid = false;
        }

        if (!isBlankField && userController.getError("email") != null) {
            emailErrorLabel.setText(userController.getError("email"));
            isBlankField = false;
            valid = false;
        }

        if (valid && registerSuccess) {
            clearFields(emailErrorLabel, passwordErrorLabel, emailField, passwordField, confirmPasswordField,
                    confirmPasswordErrorLabel, userController);
            screenController.activate("login", stage);
        }

        if (valid && !registerSuccess) {
            labelError.setText("Password update failed, user might not be logged in.");
        }
    }

    private void onchangeInitialize(TextField emailField, PasswordField passwordField,
            PasswordField confirmPasswordField, Label confirmPasswordErrorLabel, Label emailErrorLabel,
            Label passwordErrorLabel, UserController userController, Label labelError) {
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                emailErrorLabel.setText("");
                labelError.setText("");
                userController.clearError("email");
            }
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                passwordErrorLabel.setText("");
                labelError.setText("");
                userController.clearError("password");
            }
        });

        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                confirmPasswordErrorLabel.setText("");
                labelError.setText("");
                userController.clearError("confirmPassword");
            }
        });
    }

    public RegisterScreen(Stage stage, ScreenController screenController, UserController userController) {

        Label labelError = new Label();
        labelError.getStyleClass().add("error-label");

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label emailErrorLabel = new Label();
        emailErrorLabel.getStyleClass().add("error-label");

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label passwordErrorLabel = new Label();
        passwordErrorLabel.getStyleClass().add("error-label");

        Label confirmPasswordLabel = new Label("Confirm new password:");
        PasswordField confirmPasswordField = new PasswordField();
        Label confirmPasswordErrorLabel = new Label();
        confirmPasswordErrorLabel.getStyleClass().add("error-label");

        onchangeInitialize(emailField, passwordField, confirmPasswordField, confirmPasswordErrorLabel, emailErrorLabel,
                passwordErrorLabel, userController, labelError);

        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("addButton");
        registerButton.setOnAction(event -> {
            registerButtonAction(stage, emailField, passwordField, confirmPasswordField, confirmPasswordErrorLabel,
                    emailErrorLabel, passwordErrorLabel, userController, screenController, labelError);
        });

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("appButton");
        backButton.setOnAction(event -> {
            clearFields(emailErrorLabel, passwordErrorLabel, emailField, passwordField, confirmPasswordField,
                    confirmPasswordErrorLabel, userController);
            screenController.activate("login", stage);
        });

        HBox buttonBox = new HBox(20, backButton, registerButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox form = new VBox(5, emailLabel, emailField, emailErrorLabel, passwordLabel, passwordField,
                passwordErrorLabel, confirmPasswordLabel, confirmPasswordField, confirmPasswordErrorLabel, buttonBox);
        form.setAlignment(Pos.CENTER);

        this.getChildren().add(form);
        this.setAlignment(Pos.CENTER);
    }

    public static void show(Stage stage, ScreenController screenController, UserController userController) {
        Scene scene = new Scene(new RegisterScreen(stage, screenController, userController));
        stage.setScene(scene);
        stage.show();
    }
}
