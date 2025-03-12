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

public class ForgotCredentialsScreen extends VBox {

    private void resetButtonAction(TextField emailField, PasswordField newPasswordField,
            PasswordField confirmPasswordField, Label emailError, Label newPasswordError, Label confirmPasswordError,
            UserController controller) {

        boolean valid = true;
        if (emailField.getText().isBlank()) {
            emailError.setText("Email is required");
            valid = false;
        }

        if (newPasswordField.getText().isBlank()) {
            newPasswordError.setText("Password is required");
            valid = false;
        }

        if (confirmPasswordField.getText().isBlank()) {
            confirmPasswordError.setText("Password is required");
            valid = false;
        }

        if (valid) {
            controller.updateAccount(emailField.getText(), confirmPasswordField.getText());
        }

    }

    private void onchangeInitialize(TextField emailField, PasswordField newPasswordField,
            PasswordField confirmPasswordField, Label emailError, Label newPasswordError, Label confirmPasswordError) {
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                emailError.setText("");
            }
        });

        newPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                newPasswordError.setText("");
            }
        });

        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                confirmPasswordError.setText("");
            }
        });
    }

    public ForgotCredentialsScreen(Stage stage, ScreenController screenController, UserController controller) {

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

        onchangeInitialize(emailField, newPasswordField, confirmPasswordField, emailError, newPasswordError,
                confirmPasswordError);

        Button resetButton = new Button("Reset");
        resetButton.getStyleClass().add("button");
        resetButton.setOnAction(event -> {
            resetButtonAction(emailField, newPasswordField, confirmPasswordField, emailError, newPasswordError,
                    confirmPasswordError, controller);
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

    public static void show(Stage primaryStage, ScreenController screenController, UserController controller) {
        Scene scene = new Scene(new ForgotCredentialsScreen(primaryStage, screenController, controller), 700, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Reset password");
        primaryStage.show();
    }

}
