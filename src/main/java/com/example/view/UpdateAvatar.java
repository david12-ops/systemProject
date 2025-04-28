package com.example.view;

import java.io.File;

import com.example.components.ImageDropZone;
import com.example.constroller.MessageController;
import com.example.constroller.ScreenController;
import com.example.constroller.UserController;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UpdateAvatar extends VBox {
    public UpdateAvatar(Stage stage, ScreenController screenController, UserController userController,
            MessageController messageController) {
        Label dropDownLabel = new Label("Drop new image profile");
        dropDownLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ImageDropZone dropZone = new ImageDropZone();

        dropZone.setOnImageDropped(image -> {
            try {
                File file = new File(new java.net.URI(image.getUrl()));
                userController.updateImageProfile(file);
                dropZone.clear();
                screenController.updateScreen("main",
                        new MainScreen(stage, screenController, userController, messageController));
                screenController.updateScreen("switchUser",
                        new SwitchUserScreen(stage, screenController, userController, messageController));
                screenController.activate("main", stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button defaultButton = new Button("Default profile");
        defaultButton.getStyleClass().add("button");
        defaultButton.setOnAction(event -> {
            userController.updateImageProfile(null);
            screenController.updateScreen("main",
                    new MainScreen(stage, screenController, userController, messageController));
            screenController.updateScreen("switchUser",
                    new SwitchUserScreen(stage, screenController, userController, messageController));
            screenController.activate("main", stage);
        });

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("button");
        backButton.setOnAction(event -> {
            screenController.activate("main", stage);
        });

        dropZone.setPrefSize(200, 200);
        dropZone.setMaxWidth(200);

        HBox buttonBox = new HBox(20, backButton, defaultButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox imageBox = new VBox(10, dropDownLabel, dropZone, buttonBox);
        imageBox.setAlignment(Pos.CENTER);
        VBox.setVgrow(imageBox, Priority.ALWAYS);

        this.getChildren().add(imageBox);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
    }

    public static void show(Stage stage, ScreenController screenController, UserController userController,
            MessageController messageController) {
        Scene scene = new Scene(new UpdateAvatar(stage, screenController, userController, messageController));
        stage.setScene(scene);
        stage.show();
    }
}
