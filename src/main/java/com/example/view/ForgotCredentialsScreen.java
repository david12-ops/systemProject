package com.example.view;

import com.example.controller.ScreenController;
import com.example.controller.UserController;
import com.example.model.UserToken;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ForgotCredentialsScreen extends VBox {

    private void clearFields(PasswordField newPasswordField, PasswordField confirmPasswordField,
            PasswordField passwordField, TextField emailField, UserController userController, Label emailErrorLabel,
            Label passwordErrorLabel, Label newPasswordErrorLabel, Label confirmPasswordErrorLabel, Label labelError) {

        if (passwordErrorLabel != null && passwordField != null) {
            passwordField.clear();
            passwordErrorLabel.setText("");
            userController.clearError("password");
        }

        if (newPasswordErrorLabel != null && newPasswordField != null) {
            newPasswordField.clear();
            newPasswordErrorLabel.setText("");
            userController.clearError("newPassword");
        }

        if (confirmPasswordErrorLabel != null && confirmPasswordField != null) {
            confirmPasswordField.clear();
            confirmPasswordErrorLabel.setText("");
            userController.clearError("confirmPassword");
        }

        if (emailErrorLabel != null && emailField != null) {
            emailField.clear();
            emailErrorLabel.setText("");
            userController.clearError("email");
        }

        labelError.setText("");

    }

    private VBox createVbox(UserToken userToken, Label labelError, Label emailLabel, TextField emailField,
            Label emailErrorLabel, Label passwordLabel, PasswordField passwordField, Label passwordErrorLabel,
            Label newPasswordLabel, PasswordField newPasswordField, Label newPasswordErrorLabel,
            Label confirmPasswordLabel, PasswordField confirmPasswordField, Label confirmPasswordErrorLabel,
            HBox buttonBox) {
        return userToken != null
                ? new VBox(5, labelError, newPasswordLabel, newPasswordField, newPasswordErrorLabel,
                        confirmPasswordLabel, confirmPasswordField, confirmPasswordErrorLabel, buttonBox)
                : new VBox(5, labelError, emailLabel, emailField, emailErrorLabel, passwordLabel, passwordField,
                        passwordErrorLabel, newPasswordLabel, newPasswordField, newPasswordErrorLabel,
                        confirmPasswordLabel, confirmPasswordField, confirmPasswordErrorLabel, buttonBox);
    }

    private void resetButtonAction(Stage stage, TextField emailField, PasswordField passwordField,
            PasswordField newPasswordField, PasswordField confirmPasswordField, Label emailErrorLabel,
            Label passwordErrorLabel, Label newPasswordErrorLabel, Label confirmPasswordErrorLabel,
            UserController userController, ScreenController screenController, Label labelError, UserToken userToken) {

        boolean isBlankField = false;
        boolean valid = true;

        if (userToken == null) {
            if (emailField.getText().isBlank()) {
                emailErrorLabel.setText("Email is required");
                isBlankField = true;
                valid = false;
            }

            if (passwordField.getText().isBlank()) {
                passwordErrorLabel.setText("Current password is required");
                isBlankField = true;
                valid = false;
            }
        }

        if (newPasswordField.getText().isBlank()) {
            newPasswordErrorLabel.setText("New password is required");
            isBlankField = true;
            valid = false;
        }

        if (confirmPasswordField.getText().isBlank()) {
            confirmPasswordErrorLabel.setText("Confirmation password is required");
            isBlankField = true;
            valid = false;
        }

        if (userToken != null) {
            boolean updateSuccess = userController.updateLoggedInAccount(newPasswordField.getText(),
                    confirmPasswordField.getText());

            if (!isBlankField && userController.getError("newPassword") != null) {
                newPasswordErrorLabel.setText(userController.getError("newPassword"));
                isBlankField = false;
                valid = false;
            }

            if (!isBlankField && userController.getError("confirmPassword") != null) {
                confirmPasswordErrorLabel.setText(userController.getError("confirmPassword"));
                isBlankField = false;
                valid = false;
            }

            if (valid && updateSuccess) {
                clearFields(newPasswordField, confirmPasswordField, null, null, userController, null, null,
                        newPasswordErrorLabel, confirmPasswordErrorLabel, labelError);
                userController.logOut();
                screenController.activate("login", stage);
            }

            if (valid && !updateSuccess) {
                labelError.setText("Password update failed, user might not be logged in.");
            }
        } else {
            boolean updateSuccess = userController.updateNotLoggedAccount(emailField.getText(), passwordField.getText(),
                    newPasswordField.getText(), confirmPasswordField.getText());

            if (!isBlankField && userController.getError("newPassword") != null) {
                newPasswordErrorLabel.setText(userController.getError("newPassword"));
                isBlankField = false;
                valid = false;
            }

            if (!isBlankField && userController.getError("confirmPassword") != null) {
                confirmPasswordErrorLabel.setText(userController.getError("confirmPassword"));
                isBlankField = false;
                valid = false;
            }

            if (valid && updateSuccess) {
                clearFields(newPasswordField, confirmPasswordField, passwordField, emailField, userController,
                        emailErrorLabel, passwordErrorLabel, newPasswordErrorLabel, confirmPasswordErrorLabel,
                        labelError);
                screenController.activate("login", stage);
            }

            if (valid && !updateSuccess) {
                labelError.setText("Password update failed, user might not be logged in.");
            }
        }

    }

    private void onchangeInitialize(TextField emailField, PasswordField passwordField, PasswordField newPasswordField,
            PasswordField confirmPasswordField, Label emailErrorLabel, Label passwordErrorLabel,
            Label newPasswordErrorLabel, Label confirmPasswordErrorLabel, Label labelError,
            UserController userController) {
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                labelError.setText("");
                emailErrorLabel.setText("");
            }
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                labelError.setText("");
                passwordErrorLabel.setText("");
            }
        });

        newPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                newPasswordErrorLabel.setText("");
                labelError.setText("");
                userController.clearError("newPassword");
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

    public ForgotCredentialsScreen(Stage stage, ScreenController screenController, UserController userController) {

        UserToken userToken = userController.getLoggedUser();

        Label labelError = new Label();
        labelError.getStyleClass().add("error-label");

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label emailErrorLabel = new Label();
        emailErrorLabel.getStyleClass().add("error-label");

        Label passwordLabel = new Label("Current password:");
        PasswordField passwordField = new PasswordField();
        Label passwordErrorLabel = new Label();
        passwordErrorLabel.getStyleClass().add("error-label");

        Label newPasswordLabel = new Label("New password:");
        PasswordField newPasswordField = new PasswordField();
        Label newPasswordErrorLabel = new Label();
        newPasswordErrorLabel.getStyleClass().add("error-label");

        Label confirmPasswordLabel = new Label("Confirm new password:");
        PasswordField confirmPasswordField = new PasswordField();
        Label confirmPasswordErrorLabel = new Label();
        confirmPasswordErrorLabel.getStyleClass().add("error-label");

        onchangeInitialize(emailField, passwordField, newPasswordField, confirmPasswordField, emailErrorLabel,
                passwordErrorLabel, newPasswordErrorLabel, confirmPasswordErrorLabel, labelError, userController);

        Button resetButton = new Button("Reset");
        resetButton.getStyleClass().add("updateButton");
        resetButton.setOnAction(event -> {
            resetButtonAction(stage, emailField, passwordField, newPasswordField, confirmPasswordField, emailErrorLabel,
                    passwordErrorLabel, newPasswordErrorLabel, confirmPasswordErrorLabel, userController,
                    screenController, labelError, userToken);
        });

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("appButton");
        backButton.setOnAction(event -> {
            if (userToken != null) {
                clearFields(newPasswordField, confirmPasswordField, null, null, userController, null, null,
                        newPasswordErrorLabel, confirmPasswordErrorLabel, labelError);
                screenController.activate("main", stage);
            } else {
                clearFields(newPasswordField, confirmPasswordField, passwordField, emailField, userController,
                        emailErrorLabel, passwordErrorLabel, newPasswordErrorLabel, confirmPasswordErrorLabel,
                        labelError);
                screenController.activate("login", stage);
            }
        });

        HBox buttonBox = new HBox(20, backButton, resetButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox form = createVbox(userToken, labelError, emailLabel, emailField, emailErrorLabel, passwordLabel,
                passwordField, passwordErrorLabel, newPasswordLabel, newPasswordField, newPasswordErrorLabel,
                confirmPasswordLabel, confirmPasswordField, confirmPasswordErrorLabel, buttonBox);

        form.setAlignment(Pos.CENTER);

        this.getChildren().add(form);
        this.setAlignment(Pos.CENTER);
    }

    public static void show(Stage stage, ScreenController screenController, UserController userController) {
        Scene scene = new Scene(new ForgotCredentialsScreen(stage, screenController, userController));
        stage.setScene(scene);
        stage.show();
    }

}
