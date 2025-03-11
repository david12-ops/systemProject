package com.example.view;

import java.util.HashMap;

import com.example.constroller.ScreenController;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ForgotCredentialsScreen extends VBox {

    private void resetButtonAction(TextField emailField, PasswordField newPasswordField, 
    PasswordField confirmPasswordField, Label emailError, Label newPasswordError, Label confirmPasswordError, ) {
        if(emailField.getText())
    }

    public ForgotCredentialsScreen(Stage stage, ScreenController screenController) {

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label emailError = new Label();
        emailError.getStyleClass().add("error-label");

        Label newPasswordLabel = new Label("New password:");
        PasswordField newPasswordField = new PasswordField();
        Label newPasswordError = new Label();
        newPasswordError.getStyleClass().add("error-label");

        Label confirmPasswordLabel = new Label("Confirm new password:");
        PasswordField confirmPasswordField = new PasswordField();
        Label confirmPasswordError = new Label();
        confirmPasswordError.getStyleClass().add("error-label");

        Button resetButton = new Button("Reset");
        resetButton.getStyleClass().add("button");
        resetButton.setOnAction(event -> {
            // Validation example
            // emailError.setText(emailField.getText().isEmpty() ? "Email is required" :
            // "");
            // passwordError.setText(passwordField.getText().isEmpty() ? "Password is
            // required" : "");
            resetButtonAction(emailField, )
        });

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("button");
        backButton.setOnAction(event -> {
            screenController.activate("login");
        });

        VBox form = new VBox(5, emailLabel, emailField, newPasswordLabel, newPasswordField, newPasswordError,
                confirmPasswordLabel, confirmPasswordField, confirmPasswordError, resetButton, backButton);
        form.setAlignment(Pos.CENTER);

        this.getChildren().add(form);
        this.setAlignment(Pos.CENTER);
    }

    public static void show(Stage primaryStage, ScreenController screenController) {
        Scene scene = new Scene(new LoginScreen(primaryStage, screenController), 800, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Reset password");
        primaryStage.show();
    }

}
