package com.example.view;

import java.util.List;

import com.example.components.CostumeGridPane;
import com.example.components.Layout;
import com.example.constroller.MessageController;
import com.example.constroller.ScreenController;
import com.example.constroller.UserController;
import com.example.model.User;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SwitchUserScreen extends VBox {

    public SwitchUserScreen(Stage stage, ScreenController screenController, UserController userController,
            MessageController messageController) {

        List<User> users = userController.getAllUserAccounts();
        Layout layout = null;
        VBox content = null;

        Label textInfo = new Label("No another accounts found");
        textInfo.setStyle("-fx-text-fill: orangered; -fx-font-size: 30px;");
        textInfo.setAlignment(Pos.CENTER);
        textInfo.setMaxWidth(Double.MAX_VALUE);

        Label textTitle = new Label("Your accounts");
        textTitle.setStyle("-fx-text-fill: rgb(244, 160, 4); -fx-font-size: 30px;");
        textTitle.setAlignment(Pos.CENTER);
        textTitle.setMaxWidth(Double.MAX_VALUE);

        if (users != null && users.size() > 0) {

            CostumeGridPane gridPane = new CostumeGridPane(screenController, userController, messageController, stage);
            content = new VBox(textTitle, gridPane);

            layout = new Layout(stage, content, screenController, userController, messageController);
        } else {
            HBox textInfHBox = new HBox(textInfo);
            textInfHBox.setAlignment(Pos.CENTER);

            content = new VBox(20, textTitle, textInfHBox);

            layout = new Layout(stage, content, screenController, userController, messageController);
        }

        this.getChildren().add(layout);
    }

    public static void show(Stage stage, ScreenController screenController, UserController userController,
            MessageController messageController) {
        Scene scene = new Scene(new SwitchUserScreen(stage, screenController, userController, messageController));
        stage.setScene(scene);
        stage.show();
    }

}
