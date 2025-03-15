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

        Scene scene = new Scene(new LoginScreen(primaryStage, screenController, userController), 400, 300);
        screenController.setScene(scene);

        screenController.addScreen("login", new LoginScreen(primaryStage, screenController, userController));
        screenController.addScreen("register", new RegisterScreen(primaryStage, screenController, userController));
        screenController.addScreen("reset",
                new ForgotCredentialsScreen(primaryStage, screenController, userController));

        scene.getStylesheets().add(getClass().getResource("/styles/form.css").toExternalForm());

        screenController.activate("login", primaryStage);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
