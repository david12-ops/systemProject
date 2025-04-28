package com.example.view;

import com.example.constroller.ScreenController;
import com.example.constroller.UserController;
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
            PasswordField passwordField, TextField emailField, UserController userController, Label emailError,
            Label passwordError, Label newPasswordError, Label confirmPasswordError, Label labelError) {

        if (passwordError != null && passwordField != null) {
            passwordField.clear();
            passwordError.setText("");
            userController.clearError("password");
        }

        if (newPasswordError != null && newPasswordField != null) {
            newPasswordField.clear();
            newPasswordError.setText("");
            userController.clearError("newPassword");
        }

        if (confirmPasswordError != null && confirmPasswordField != null) {
            confirmPasswordField.clear();
            confirmPasswordError.setText("");
            userController.clearError("confirmPassword");
        }

        if (emailError != null && emailField != null) {
            emailField.clear();
            emailError.setText("");
            userController.clearError("email");
        }

        labelError.setText("");

    }

    private VBox createVbox(UserToken userToken, Label labelError, Label emailLabel, TextField emailField,
            Label emailError, Label passwordLabel, PasswordField passwordField, Label passwordError,
            Label newPasswordLabel, PasswordField newPasswordField, Label newPasswordError, Label confirmPasswordLabel,
            PasswordField confirmPasswordField, Label confirmPasswordError, HBox buttonBox) {
        return userToken != null
                ? new VBox(5, labelError, newPasswordLabel, newPasswordField, newPasswordError, confirmPasswordLabel,
                        confirmPasswordField, confirmPasswordError, buttonBox)
                : new VBox(5, labelError, emailLabel, emailField, emailError, passwordLabel, passwordField,
                        passwordError, newPasswordLabel, newPasswordField, newPasswordError, confirmPasswordLabel,
                        confirmPasswordField, confirmPasswordError, buttonBox);
    }

    private void resetButtonAction(Stage stage, TextField emailField, PasswordField passwordField,
            PasswordField newPasswordField, PasswordField confirmPasswordField, Label emailError, Label passwordError,
            Label newPasswordError, Label confirmPasswordError, UserController userController,
            ScreenController screenController, Label labelError, UserToken userToken) {

        boolean isBlankField = false;
        boolean valid = true;

        if (userToken == null) {
            if (emailField.getText().isBlank()) {
                emailError.setText("Email is required");
                isBlankField = true;
                valid = false;
            }

            if (passwordField.getText().isBlank()) {
                passwordError.setText("Current password is required");
                isBlankField = true;
                valid = false;
            }
        }

        if (newPasswordField.getText().isBlank()) {
            newPasswordError.setText("New password is required");
            isBlankField = true;
            valid = false;
        }

        if (confirmPasswordField.getText().isBlank()) {
            confirmPasswordError.setText("Confirmation password is required");
            isBlankField = true;
            valid = false;
        }

        if (userToken != null) {
            boolean updateSuccess = userController.updateLoggedInAccount(newPasswordField.getText(),
                    confirmPasswordField.getText());

            if (!isBlankField && userController.getError("newPassword") != null) {
                newPasswordError.setText(userController.getError("newPassword"));
                isBlankField = false;
                valid = false;
            }

            if (!isBlankField && userController.getError("confirmPassword") != null) {
                confirmPasswordError.setText(userController.getError("confirmPassword"));
                isBlankField = false;
                valid = false;
            }

            if (valid && updateSuccess) {
                clearFields(newPasswordField, confirmPasswordField, null, null, userController, null, null,
                        newPasswordError, confirmPasswordError, labelError);
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
                newPasswordError.setText(userController.getError("newPassword"));
                isBlankField = false;
                valid = false;
            }

            if (!isBlankField && userController.getError("confirmPassword") != null) {
                confirmPasswordError.setText(userController.getError("confirmPassword"));
                isBlankField = false;
                valid = false;
            }

            if (valid && updateSuccess) {
                clearFields(newPasswordField, confirmPasswordField, passwordField, emailField, userController,
                        emailError, passwordError, newPasswordError, confirmPasswordError, labelError);
                screenController.activate("login", stage);
            }

            if (valid && !updateSuccess) {
                labelError.setText("Password update failed, user might not be logged in.");
            }
        }

    }

    private void onchangeInitialize(TextField emailField, PasswordField passwordField, PasswordField newPasswordField,
            PasswordField confirmPasswordField, Label emailError, Label passwordError, Label newPasswordError,
            Label confirmPasswordError, Label labelError, UserController userController) {
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                labelError.setText("");
                emailError.setText("");
            }
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                labelError.setText("");
                passwordError.setText("");
            }
        });

        newPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                newPasswordError.setText("");
                labelError.setText("");
                userController.clearError("newPassword");
            }
        });

        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                confirmPasswordError.setText("");
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
        Label emailError = new Label();
        emailError.getStyleClass().add("error-label");

        Label passwordLabel = new Label("Current password:");
        PasswordField passwordField = new PasswordField();
        Label passwordError = new Label();
        passwordError.getStyleClass().add("error-label");

        Label newPasswordLabel = new Label("New password:");
        PasswordField newPasswordField = new PasswordField();
        Label newPasswordError = new Label();
        newPasswordError.getStyleClass().add("error-label");

        Label confirmPasswordLabel = new Label("Confirm new password:");
        PasswordField confirmPasswordField = new PasswordField();
        Label confirmPasswordError = new Label();
        confirmPasswordError.getStyleClass().add("error-label");

        onchangeInitialize(emailField, passwordField, newPasswordField, confirmPasswordField, emailError, passwordError,
                newPasswordError, confirmPasswordError, labelError, userController);

        Button resetButton = new Button("Reset");
        resetButton.getStyleClass().add("button");
        resetButton.setOnAction(event -> {
            resetButtonAction(stage, emailField, passwordField, newPasswordField, confirmPasswordField, emailError,
                    passwordError, newPasswordError, confirmPasswordError, userController, screenController, labelError,
                    userToken);
        });

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("button");
        backButton.setOnAction(event -> {
            if (userToken != null) {
                clearFields(newPasswordField, confirmPasswordField, null, null, userController, null, null,
                        newPasswordError, confirmPasswordError, labelError);
                screenController.activate("main", stage);
            } else {
                clearFields(newPasswordField, confirmPasswordField, passwordField, emailField, userController,
                        emailError, passwordError, newPasswordError, confirmPasswordError, labelError);
                screenController.activate("login", stage);
            }
        });

        HBox buttonBox = new HBox(20, backButton, resetButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox form = createVbox(userToken, labelError, emailLabel, emailField, emailError, passwordLabel, passwordField,
                passwordError, newPasswordLabel, newPasswordField, newPasswordError, confirmPasswordLabel,
                confirmPasswordField, confirmPasswordError, buttonBox);

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
