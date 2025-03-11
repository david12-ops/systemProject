package com.example.view;

import com.example.constroller.ScreenController;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginScreen extends VBox {

    private void loginButtonAction() {

    }

    public LoginScreen(Stage stage, ScreenController screenController) {

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label emailError = new Label();
        emailError.getStyleClass().add("error-label");

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label passwordError = new Label();
        passwordError.getStyleClass().add("error-label");

        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("button");
        loginButton.setOnAction(event -> {
            // Validation example
            emailError.setText(emailField.getText().isEmpty() ? "Email is required" : "");
            passwordError.setText(passwordField.getText().isEmpty() ? "Password is required" : "");
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

    public static void show(Stage primaryStage, ScreenController screenController) {
        Scene scene = new Scene(new LoginScreen(primaryStage, screenController), 300, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }
}
