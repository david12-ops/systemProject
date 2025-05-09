package com.example.view;

import com.example.controller.MessageController;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class AddAnotherAccountScreen extends VBox {

    private Label createErrorLabel() {
        Label label = new Label();
        label.setWrapText(true);
        label.setMaxWidth(250);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setAlignment(Pos.CENTER);
        label.getStyleClass().add("error-label");
        return label;
    }

    private void clearFields(Label emailErrorLabel, Label passwordErrorLabel, Label confirmPasswordErrorLabel,
            TextField emailField, PasswordField passwordField, PasswordField confirmPasswordField,
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

    private void addButtonAction(Stage stage, TextField emailField, PasswordField passwordField,
            PasswordField confirmPasswordField, Label confirmPasswordErrorLabel, Label emailErrorLabel,
            Label passwordErrorLabel, UserController userController, ScreenController screenController,
            MessageController messageControll, Label labelError) {

        boolean isBlankField = false;
        boolean valid = true;
        boolean addedAcount = false;

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

        if (!isBlankField) {
            addedAcount = userController.addAnotherAccount(emailField.getText(), passwordField.getText(),
                    confirmPasswordField.getText());
        }

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

        if (valid && addedAcount) {
            clearFields(emailErrorLabel, passwordErrorLabel, confirmPasswordErrorLabel, emailField, passwordField,
                    confirmPasswordField, userController);
            screenController.updateScreen("switchUser",
                    new SwitchUserScreen(stage, screenController, userController, messageControll));
            screenController.activate("main", stage);
        }

        if (valid && !addedAcount) {
            String error = userController.getError("unexpected");
            labelError.setText(error == null
                    ? "Adding new account failed due to an unexpected error or session issue. Please try again or contact support"
                    : error);
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
                userController.clearError("confirmPassword");
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

    public AddAnotherAccountScreen(Stage stage, ScreenController screenController, UserController userController,
            MessageController messageControll) {

        Label labelError = createErrorLabel();

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.getStyleClass().add("text-field");

        Label emailErrorLabel = createErrorLabel();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.getStyleClass().add("password-field");

        Label passwordErrorLabel = createErrorLabel();

        Label confirmPasswordLabel = new Label("Confirm password:");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.getStyleClass().add("password-field");

        Label confirmPasswordErrorLabel = createErrorLabel();

        onchangeInitialize(emailField, passwordField, confirmPasswordField, confirmPasswordErrorLabel, emailErrorLabel,
                passwordErrorLabel, userController, labelError);

        Button addButton = new Button("Add");
        addButton.getStyleClass().add("addButton");
        addButton.setOnAction(e -> {
            addButtonAction(stage, emailField, passwordField, confirmPasswordField, confirmPasswordErrorLabel,
                    emailErrorLabel, passwordErrorLabel, userController, screenController, messageControll, labelError);
        });

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("appButton");
        backButton.setOnAction(e -> {
            clearFields(emailErrorLabel, passwordErrorLabel, confirmPasswordErrorLabel, emailField, passwordField,
                    confirmPasswordField, userController);
            screenController.activate("main", stage);
        });

        HBox buttonBox = new HBox(20, backButton, addButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox form = new VBox(5, labelError, emailLabel, emailField, emailErrorLabel, passwordLabel, passwordField,
                passwordErrorLabel, confirmPasswordLabel, confirmPasswordField, confirmPasswordErrorLabel, buttonBox);

        form.setAlignment(Pos.CENTER);

        this.getChildren().add(form);
        this.setAlignment(Pos.CENTER);
    }

    public static void show(Stage stage, ScreenController screenController, UserController userController,
            MessageController messageControll) {
        Scene scene = new Scene(new AddAnotherAccountScreen(stage, screenController, userController, messageControll));
        stage.setScene(scene);
        stage.show();
    }

}
