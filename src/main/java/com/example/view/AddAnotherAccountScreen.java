package com.example.view;

import com.example.constroller.ScreenController;
import com.example.constroller.UserController;
import com.example.utils.interfaces.ErrorManagement;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddAnotherAccountScreen extends VBox {

    private void clearFields(Label emailError, Label passwordError, Label confirmPasswordError, TextField emailField,
            PasswordField passwordField, PasswordField confirmPasswordField, ErrorManagement errorHandler) {
        emailError.setText("");
        passwordError.setText("");
        emailField.clear();
        passwordField.clear();
        confirmPasswordError.setText("");
        confirmPasswordField.setText("");
        errorHandler.removeError("confirmPassword");
        errorHandler.removeError("email");
        errorHandler.removeError("password");
    }

    private void addButtonAction(Stage stage, TextField emailField, PasswordField passwordField,
            PasswordField confirmPasswordField, Label confirmPasswordError, Label emailError, Label passwordError,
            UserController userController, ScreenController screenController, ErrorManagement errorHandler,
            Label labelError) {

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

        userController.addAnotherAccount(emailField.getText(), passwordField.getText(), confirmPasswordField.getText());

        if (!isBlankField && errorHandler.getError("password") != null) {
            passwordError.setText(errorHandler.getError("password"));
            isBlankField = false;
            valid = false;
        }

        if (!isBlankField && errorHandler.getError("confirmPassword") != null) {
            confirmPasswordError.setText(errorHandler.getError("confirmPassword"));
            isBlankField = false;
            valid = false;
        }

        if (!isBlankField && errorHandler.getError("email") != null) {
            emailError.setText(errorHandler.getError("email"));
            isBlankField = false;
            valid = false;
        }

        if (valid) {
            clearFields(emailError, passwordError, confirmPasswordError, emailField, passwordField,
                    confirmPasswordField, errorHandler);
            screenController.activate("main", stage);
        }
    }

    private void onchangeInitialize(TextField emailField, PasswordField passwordField,
            PasswordField confirmPasswordField, Label confirmPasswordError, Label emailError, Label passwordError,
            ErrorManagement errorHandler, Label labelError) {
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                emailError.setText("");
                labelError.setText("");
                errorHandler.removeError("email");
            }
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                passwordError.setText("");
                labelError.setText("");
                errorHandler.removeError("password");
            }
        });

        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                confirmPasswordError.setText("");
                labelError.setText("");
                errorHandler.removeError("confirmPassword");
            }
        });
    }

    public AddAnotherAccountScreen(Stage stage, ScreenController screenController, UserController userController) {
        ErrorManagement errorHandler = userController.getErrorHandler();

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

        Label confirmPasswordLabel = new Label("Confirm password:");
        PasswordField confirmPasswordField = new PasswordField();
        Label confirmPasswordError = new Label();
        confirmPasswordError.getStyleClass().add("error-label");

        onchangeInitialize(emailField, passwordField, confirmPasswordField, confirmPasswordError, emailError,
                passwordError, errorHandler, labelError);

        Button addButton = new Button("Add");
        addButton.getStyleClass().add("button");
        addButton.setOnAction(e -> {
            addButtonAction(stage, emailField, passwordField, confirmPasswordField, confirmPasswordError, emailError,
                    passwordError, userController, screenController, errorHandler, labelError);
        });

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("button");
        backButton.setOnAction(e -> {
            clearFields(emailError, passwordError, confirmPasswordError, emailField, passwordField,
                    confirmPasswordField, errorHandler);
            screenController.activate("main", stage);
        });

        HBox buttonBox = new HBox(20, backButton, addButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox form = new VBox(5, labelError, emailLabel, emailField, emailError, passwordLabel, passwordField,
                passwordError, confirmPasswordLabel, confirmPasswordField, confirmPasswordError, buttonBox);

        form.setAlignment(Pos.CENTER);

        this.getChildren().add(form);
        this.setAlignment(Pos.CENTER);
    }

    public static void show(Stage stage, ScreenController screenController, UserController userController) {
        Scene scene = new Scene(new AddAnotherAccountScreen(stage, screenController, userController));
        stage.setScene(scene);
        stage.show();
    }

}
