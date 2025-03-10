package com.example;

import com.example.constroller.UserController;
import com.example.model.UserModel;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private UserModel model = new UserModel();
    private UserController controller = new UserController(model);

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        // Login Tab
        Tab loginTab = new Tab("Login", createLoginForm(primaryStage));
        loginTab.setClosable(false);

        // Registration Tab
        Tab registerTab = new Tab("Register", createRegisterForm(primaryStage));
        registerTab.setClosable(false);

        tabPane.getTabs().addAll(loginTab, registerTab);

        VBox root = new VBox(tabPane);
        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/styles/form.css").toExternalForm());
        primaryStage.setTitle("User Authentication");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createLoginForm(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.getStyleClass().add("form-grid");

        Label userLabel = new Label("Email:");
        TextField userField = new TextField();

        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            controller.login(userField.getText(), passField.getText());
            if (controller.getLoggedUser() != null) {
                System.out.println("Logged");
            } else {
                System.out.println("Invalid");
            }
        });

        grid.setStyle("-fx-padding: 20; -fx-alignment: center;");
        loginButton.setStyle("-fx-padding: 10;");

        grid.add(userLabel, 0, 0);
        grid.add(userField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passField, 1, 1);
        grid.add(loginButton, 1, 2);

        return grid;
    }

    private GridPane createRegisterForm(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.getStyleClass().add("form-grid");

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> {
            System.out.println("Registered");
            controller.register(emailField.getText().trim(), passField.getText().trim());
        });

        Button removeButton = new Button("Remove");
        removeButton.setOnAction(e -> {
            emailField.clear();
            passField.clear();
            controller.removeAccount(controller.getLoggedUser());
        });

        grid.setStyle("-fx-padding: 20; -fx-alignment: center;");
        registerButton.setStyle("-fx-padding: 10;");

        grid.add(emailLabel, 0, 1);
        grid.add(removeButton, 0, 3);
        grid.add(emailField, 1, 1);
        grid.add(passLabel, 0, 2);
        grid.add(passField, 1, 2);
        grid.add(registerButton, 1, 3);

        return grid;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
