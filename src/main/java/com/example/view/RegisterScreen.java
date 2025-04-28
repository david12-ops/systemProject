package com.example.view;

import com.example.constroller.ScreenController;
import com.example.constroller.UserController;

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

    private void clearFields(Label emailError, Label passwordError, TextField emailField, PasswordField passwordField,
            PasswordField confirmPasswordField, Label confirmPasswordError, UserController userController) {
        emailError.setText("");
        passwordError.setText("");
        emailField.clear();
        passwordField.clear();
        confirmPasswordError.setText("");
        confirmPasswordField.setText("");
        userController.clearError("confirmPassword");
        userController.clearError("email");
        userController.clearError("password");
    }

    private void registerButtonAction(Stage stage, TextField emailField, PasswordField passwordField,
            PasswordField confirmPasswordField, Label confirmPasswordError, Label emailError, Label passwordError,
            UserController userController, ScreenController screenController, Label labelError) {

        boolean isBlankField = false;
        boolean valid = true;

        if (emailField.getText().isBlank()) {
            emailError.setText("Email is required");
            isBlankField = true;
            valid = false;
        }

        if (passwordField.getText().isBlank()) {
            passwordError.setText("Password is required");
            isBlankField = true;
            valid = false;
        }

        if (confirmPasswordField.getText().isBlank()) {
            confirmPasswordError.setText("Confirmation password is required");
            isBlankField = true;
            valid = false;
        }

        boolean registerSuccess = userController.register(emailField.getText(), passwordField.getText(),
                confirmPasswordField.getText());

        if (!isBlankField && userController.getError("password") != null) {
            passwordError.setText(userController.getError("password"));
            isBlankField = false;
            valid = false;
        }

        if (!isBlankField && userController.getError("confirmPassword") != null) {
            confirmPasswordError.setText(userController.getError("confirmPassword"));
            isBlankField = false;
            valid = false;
        }

        if (!isBlankField && userController.getError("email") != null) {
            emailError.setText(userController.getError("email"));
            isBlankField = false;
            valid = false;
        }

        if (valid && registerSuccess) {
            clearFields(emailError, passwordError, emailField, passwordField, confirmPasswordField,
                    confirmPasswordError, userController);
            screenController.activate("login", stage);
        }

        if (valid && !registerSuccess) {
            labelError.setText("Password update failed, user might not be logged in.");
        }
    }

    private void onchangeInitialize(TextField emailField, PasswordField passwordField,
            PasswordField confirmPasswordField, Label confirmPasswordError, Label emailError, Label passwordError,
            UserController userController, Label labelError) {
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                emailError.setText("");
                labelError.setText("");
                userController.clearError("email");
            }
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                passwordError.setText("");
                labelError.setText("");
                userController.clearError("password");
            }
        });

        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                confirmPasswordError.setText("");
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
        Label emailError = new Label();
        emailError.getStyleClass().add("error-label");

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label passwordError = new Label();
        passwordError.getStyleClass().add("error-label");

        Label confirmPasswordLabel = new Label("Confirm new password:");
        PasswordField confirmPasswordField = new PasswordField();
        Label confirmPasswordError = new Label();
        confirmPasswordError.getStyleClass().add("error-label");

        onchangeInitialize(emailField, passwordField, confirmPasswordField, confirmPasswordError, emailError,
                passwordError, userController, labelError);

        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("button");
        registerButton.setOnAction(event -> {
            registerButtonAction(stage, emailField, passwordField, confirmPasswordField, confirmPasswordError,
                    emailError, passwordError, userController, screenController, labelError);
        });

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("button");
        backButton.setOnAction(event -> {
            clearFields(emailError, passwordError, emailField, passwordField, confirmPasswordField,
                    confirmPasswordError, userController);
            screenController.activate("login", stage);
        });

        HBox buttonBox = new HBox(20, backButton, registerButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox form = new VBox(5, emailLabel, emailField, emailError, passwordLabel, passwordField, passwordError,
                confirmPasswordLabel, confirmPasswordField, confirmPasswordError, buttonBox);
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
