package com.example;

import com.example.constroller.ScreenController;
import com.example.view.ForgotCredentialsScreen;
import com.example.view.LoginScreen;
import com.example.view.RegisterScreen;
import com.example.view.ForgotCredentialsScreen;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    // private UserModel model = new UserModel();
    // private UserController controller = new UserController(model);
    private ScreenController screenController;

    @Override
    public void start(Stage primaryStage) {
        // Create Scene and ScreenController
        Scene scene = new Scene(new LoginScreen(primaryStage, null), 400, 300);
        screenController = new ScreenController(scene);

        // Add screens
        screenController.addScreen("login", new LoginScreen(primaryStage, screenController));
        screenController.addScreen("register", new RegisterScreen(primaryStage, screenController));
        screenController.addScreen("reset", new ForgotCredentialsScreen(primaryStage, screenController));

        // Load CSS
        scene.getStylesheets().add(getClass().getResource("/styles/form.css").toExternalForm());

        // Activate first screen
        screenController.activate("login");

        // Stage setup
        primaryStage.setTitle("Authentication");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
