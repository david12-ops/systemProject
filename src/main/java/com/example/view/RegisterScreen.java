package com.example.view;

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

    private void registerButtonAction(TextField emailField, PasswordField passwordField, Label emailError,
            Label passwordError, UserController controller) {

        boolean valid = true;
        if (emailField.getText().isBlank()) {
            emailError.setText("Email is required");
            valid = false;
        }

        if (passwordField.getText().isBlank()) {
            passwordError.setText("Password is required");
            valid = false;
        }

        if (valid) {
            controller.register(emailField.getText(), passwordField.getText());
        }
    }

    private void onchangeInitialize(TextField emailField, PasswordField passwordField, Label emailError,
            Label passwordError) {
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                emailError.setText("");
            }
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                passwordError.setText("");
            }
        });
    }

    public RegisterScreen(Stage stage, ScreenController screenController, UserController controller) {

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label emailError = new Label();
        emailError.getStyleClass().add("error-label");

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label passwordError = new Label();
        passwordError.getStyleClass().add("error-label");

        onchangeInitialize(emailField, passwordField, emailError, passwordError);

        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("button");
        registerButton.setOnAction(event -> {
            registerButtonAction(emailField, passwordField, emailError, passwordError, controller);
        });

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("button");
        backButton.setOnAction(event -> {
            screenController.activate("login");
        });

        VBox form = new VBox(5, emailLabel, emailField, emailError, passwordLabel, passwordField, passwordError,
                registerButton, backButton);
        form.setAlignment(Pos.CENTER);

        this.getChildren().add(form);
        this.setAlignment(Pos.CENTER);
    }

    public static void show(Stage primaryStage, ScreenController screenController, UserController controller) {
        Scene scene = new Scene(new RegisterScreen(primaryStage, screenController, controller), 300, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Register");
        primaryStage.show();
    }
}
