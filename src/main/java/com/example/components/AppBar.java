package com.example.components;

import java.io.File;

import com.example.constroller.UserController;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class AppBar extends HBox {
    private final Label title;
    private final Avatar avatar;
    private final Button logOutButton;

    public AppBar(String appTitle, UserController userController) {
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER_LEFT);
        this.getStyleClass().add("appBar");

        title = new Label(appTitle);
        title.setTextFill(Color.WHITE);
        title.getStyleClass().add("title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        avatar = new Avatar(userController);
        avatar.setImage(userController.getImageProfile());

        logOutButton = new Button("Logout");
        logOutButton.getStyleClass().add("button");
        this.getChildren().addAll(title, spacer, avatar, logOutButton);
    }

    public void setAvatarImage(Image newImage) {
        avatar.setImage(newImage);
    }

    public Button getLogoutButton() {
        return logOutButton;
    }

    public void setTitle(String newTitle) {
        title.setText(newTitle);
    }
}
