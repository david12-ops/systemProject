package com.example.view;

import com.example.controller.MessageController;
import com.example.controller.ScreenController;
import com.example.controller.UserController;
import com.example.utils.interfaces.GuiHelperFunctions;

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

public class AddAnotherAccountScreen extends VBox implements GuiHelperFunctions {

    @Override
    public Label createErrorLabel() {
        Label label = new Label();
        label.setWrapText(true);
        label.setMaxWidth(250);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setAlignment(Pos.CENTER);
        label.getStyleClass().add("error-label");
        return label;
    }

    @Override
    public void showIfError(String error, Label label) {
        label.setText(error != null ? error : "");
    }

    @Override
    public void clearErrorLabels(Label... labels) {
        for (Label label : labels) {
            label.setText("");
        }
    }

    private void clearFields(Label emailErrorLabel, Label passwordErrorLabel, Label confirmPasswordErrorLabel,
            TextField emailField, PasswordField passwordField, PasswordField confirmPasswordField,
            UserController userController) {

        clearErrorLabels(emailErrorLabel, passwordErrorLabel, confirmPasswordErrorLabel);
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.setText("");
        userController.clearError("confirmPassword");
        userController.clearError("email");
        userController.clearError("password");
    }

    private void addButtonAction(Stage stage, TextField emailField, PasswordField passwordField,
            PasswordField confirmPasswordField, Label confirmPasswordErrorLabel, Label emailErrorLabel,
            Label passwordErrorLabel, UserController userController, ScreenController screenController,
            MessageController messageControll, Label labelError) {

        boolean valid = true;
        labelError.setText("");
        clearErrorLabels(emailErrorLabel, passwordErrorLabel, confirmPasswordErrorLabel);

        if (emailField.getText().isBlank()) {
            emailErrorLabel.setText("Email is required");
            valid = false;
        }
        if (passwordField.getText().isBlank()) {
            passwordErrorLabel.setText("Password is required");
            valid = false;
        }
        if (confirmPasswordField.getText().isBlank()) {
            confirmPasswordErrorLabel.setText("Confirmation password is required");
            valid = false;
        }

        boolean addedAccount = false;

        if (valid) {
            addedAccount = userController.addAnotherAccount(emailField.getText(), passwordField.getText(),
                    confirmPasswordField.getText());

            showIfError(userController.getError("email"), emailErrorLabel);
            showIfError(userController.getError("password"), passwordErrorLabel);
            showIfError(userController.getError("confirmPassword"), confirmPasswordErrorLabel);

            if (userController.getError("email") != null || userController.getError("password") != null
                    || userController.getError("confirmPassword") != null) {
                valid = false;
            }
        }

        if (valid && addedAccount) {
            clearFields(emailErrorLabel, passwordErrorLabel, confirmPasswordErrorLabel, emailField, passwordField,
                    confirmPasswordField, userController);

            screenController.updateScreen("switchUser",
                    new SwitchUserScreen(stage, screenController, userController, messageControll));
            screenController.activate("main", stage);
        } else if (valid) {
            labelError.setText(
                    "Adding new account failed due to an unexpected error or session issue. Please try again or contact support");
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
