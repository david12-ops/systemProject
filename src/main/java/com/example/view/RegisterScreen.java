package com.example.view;

import java.util.HashMap;

import com.example.constroller.ScreenController;
import com.example.constroller.UserController;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterScreen extends VBox {

    private void registerButtonAction(Stage stage, TextField emailField, PasswordField passwordField, Label emailError,
            Label passwordError, UserController userController, ScreenController screenController,
            HashMap<String, String> errors) {

        boolean valid = true;

        if (emailField.getText().isBlank()) {
            emailError.setText("Email is required");
            valid = false;
        } else {
            if (errors.get("email") != null) {
                emailError.setText(errors.get("email"));
                valid = false;
            }
        }

        if (passwordField.getText().isBlank()) {
            passwordError.setText("Password is required");
            valid = false;
        } else {
            if (errors.get("password") != null) {
                passwordError.setText(errors.get("password"));
                valid = false;
            }
        }

        if (valid) {
            boolean registerSuccess = userController.register(emailField.getText(), passwordField.getText());
            if (registerSuccess) {
                emailField.clear();
                passwordField.clear();
                screenController.activate("login", stage);
            }
        }
    }

    private void onchangeInitialize(TextField emailField, PasswordField passwordField, Label emailError,
            Label passwordError, HashMap<String, String> errors) {
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                emailError.setText("");
                errors.remove("email");
            }
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                passwordError.setText("");
                errors.remove("password");
            }
        });
    }

    public RegisterScreen(Stage stage, ScreenController screenController, UserController userController) {

        HashMap<String, String> errors = userController.getInputErrors();

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label emailError = new Label();
        emailError.getStyleClass().add("error-label");

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label passwordError = new Label();
        passwordError.getStyleClass().add("error-label");

        onchangeInitialize(emailField, passwordField, emailError, passwordError, errors);

        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("button");
        registerButton.setOnAction(event -> {
            registerButtonAction(stage, emailField, passwordField, emailError, passwordError, userController,
                    screenController, errors);
        });

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("button");
        backButton.setOnAction(event -> {
            screenController.activate("login", stage);
        });

        VBox form = new VBox(5, emailLabel, emailField, emailError, passwordLabel, passwordField, passwordError,
                registerButton, backButton);
        form.setAlignment(Pos.CENTER);

        this.getChildren().add(form);
        this.setAlignment(Pos.CENTER);
    }

    public static void show(Stage primaryStage, ScreenController screenController, UserController userController) {
        Scene scene = new Scene(new RegisterScreen(primaryStage, screenController, userController));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
