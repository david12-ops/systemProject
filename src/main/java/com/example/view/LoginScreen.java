package com.example.view;

import com.example.constroller.ScreenController;
import com.example.constroller.UserController;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginScreen extends VBox {

    private void loginButtonAction(TextField emailField, PasswordField passwordField, Label emailError,
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
            controller.login(emailField.getText(), passwordField.getText());
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

    public LoginScreen(Stage stage, ScreenController screenController, UserController controller) {

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label emailError = new Label();
        emailError.getStyleClass().add("error-label");

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label passwordError = new Label();
        passwordError.getStyleClass().add("error-label");

        onchangeInitialize(emailField, passwordField, emailError, passwordError);

        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("button");
        loginButton.setOnAction(event -> {
            loginButtonAction(emailField, passwordField, emailError, passwordError, controller);
        });

        Hyperlink registerLink = new Hyperlink("Don't have an account? Register");
        registerLink.setOnAction(event -> screenController.activate("register"));

        Hyperlink resetLink = new Hyperlink("Don't remember password? Reset password");
        resetLink.setOnAction(event -> screenController.activate("reset"));

        VBox form = new VBox(5, emailLabel, emailField, emailError, passwordLabel, passwordField, passwordError,
                loginButton, registerLink, resetLink);
        form.setAlignment(Pos.CENTER);

        this.getChildren().add(form);
        this.setAlignment(Pos.CENTER);
    }

    public static void show(Stage primaryStage, ScreenController screenController, UserController controller) {
        Scene scene = new Scene(new LoginScreen(primaryStage, screenController, controller), 300, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }
}
