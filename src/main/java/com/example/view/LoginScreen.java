package com.example.view;

import java.util.HashMap;

import com.example.constroller.MessageController;
import com.example.constroller.ScreenController;
import com.example.constroller.UserController;
import com.example.model.UserToken;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginScreen extends VBox {

    private void updateScreens(ScreenController screenController, UserController userController,
            MessageController messageController, Stage stage) {
        screenController.updateScreen("main",
                new MainScreen(stage, screenController, userController, messageController));

        screenController.updateScreen("reset", new ForgotCredentialsScreen(stage, screenController, userController));
    }

    private void loginButtonAction(TextField emailField, PasswordField passwordField, Label emailError,
            Label passwordError, UserController userController, ScreenController screenController,
            MessageController messageController, HashMap<String, String> errors, Label labelError, Stage stage) {

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
            userController.login(emailField.getText(), passwordField.getText());
            UserToken userToken = userController.getLoggedUser();
            if (userToken != null) {
                emailField.clear();
                passwordField.clear();
                updateScreens(screenController, userController, messageController, stage);
                screenController.activate("main", stage);
            } else {
                labelError.setText("User not found, invalid email or password");
            }
        }

    }

    private void onchangeInitialize(TextField emailField, PasswordField passwordField, Label emailError,
            Label passwordError, HashMap<String, String> errors, Label labelError) {
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                emailError.setText("");
                labelError.setText("");
            }
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                passwordError.setText("");
                labelError.setText("");
            }
        });
    }

    public LoginScreen(Stage stage, ScreenController screenController, UserController userController,
            MessageController messageController) {

        HashMap<String, String> errors = userController.getInputErrors();

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

        onchangeInitialize(emailField, passwordField, emailError, passwordError, errors, labelError);

        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("button");
        loginButton.setOnAction(event -> {
            loginButtonAction(emailField, passwordField, emailError, passwordError, userController, screenController,
                    messageController, errors, labelError, stage);
        });

        Hyperlink registerLink = new Hyperlink("Don't have an account? Register");
        registerLink.setOnAction(event -> screenController.activate("register", stage));

        Hyperlink resetLink = new Hyperlink("Don't remember password? Reset password");
        resetLink.setOnAction(event -> screenController.activate("reset", stage));

        VBox form = new VBox(5, labelError, emailLabel, emailField, emailError, passwordLabel, passwordField,
                passwordError, loginButton, registerLink, resetLink);
        form.setAlignment(Pos.CENTER);

        this.getChildren().add(form);
        this.setAlignment(Pos.CENTER);
    }

    public static void show(Stage primaryStage, ScreenController screenController, UserController userController,
            MessageController messageController) {
        Scene scene = new Scene(new LoginScreen(primaryStage, screenController, userController, messageController));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
