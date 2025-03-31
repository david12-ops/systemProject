package com.example;

import com.example.constroller.ScreenController;
import com.example.constroller.UserController;
import com.example.model.UserModel;
import com.example.view.ForgotCredentialsScreen;
import com.example.view.LoginScreen;
import com.example.view.RegisterScreen;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private UserModel model = new UserModel();
    private UserController userController = new UserController(model);
    private ScreenController screenController;

    @Override
    public void start(Stage primaryStage) {
        screenController = new ScreenController(null);

        LoginScreen loginScreen = new LoginScreen(primaryStage, screenController, userController);
        RegisterScreen registerScreen = new RegisterScreen(primaryStage, screenController, userController);
        ForgotCredentialsScreen resetScreen = new ForgotCredentialsScreen(primaryStage, screenController,
                userController);

        Scene scene = new Scene(loginScreen, 400, 300);
        screenController.setScene(scene);

        screenController.addScreen("login", loginScreen);
        screenController.addScreen("register", registerScreen);
        screenController.addScreen("reset", resetScreen);

        scene.getStylesheets().add(getClass().getResource("/styles/form.css").toExternalForm());

        screenController.activate("login", primaryStage);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
