package com.example;

import com.example.constroller.MessageController;
import com.example.constroller.ScreenController;
import com.example.constroller.UserController;
import com.example.model.MessageModel;
import com.example.model.UserModel;
import com.example.view.ForgotCredentialsScreen;
import com.example.view.LoginScreen;
import com.example.view.MainScreen;
import com.example.view.RegisterScreen;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private UserModel userModel = new UserModel();
    private MessageModel messageModel = new MessageModel();
    private UserController userController = new UserController(userModel);
    private MessageController messageController = new MessageController(messageModel);
    private ScreenController screenController;

    @Override
    public void start(Stage primaryStage) {
        screenController = new ScreenController(null);

        LoginScreen loginScreen = new LoginScreen(primaryStage, screenController, userController);
        RegisterScreen registerScreen = new RegisterScreen(primaryStage, screenController, userController);
        ForgotCredentialsScreen resetScreen = new ForgotCredentialsScreen(primaryStage, screenController,
                userController);
        MainScreen mainScreen = new MainScreen(primaryStage, screenController, userController, messageController);

        Scene scene = new Scene(loginScreen, 400, 300);
        screenController.setScene(scene);

        screenController.addScreen("login", loginScreen);
        screenController.addScreen("register", registerScreen);
        screenController.addScreen("reset", resetScreen);
        screenController.addScreen("main", mainScreen);

        scene.getStylesheets().add(getClass().getResource("/styles/form.css").toExternalForm());

        screenController.activate("login", primaryStage);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
