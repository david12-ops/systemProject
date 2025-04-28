package com.example.components;

import com.example.constroller.ScreenController;
import com.example.constroller.UserController;
import com.example.model.User;
import com.example.model.UserToken;
import com.example.utils.ImageConvertor;
import com.example.utils.services.StateEventService;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class Avatar extends VBox {

    public Avatar(Stage stage, User user) {

        Image imgaeProfile = ImageConvertor.Base64ToImage(user.getProfileImage());
        String firstLetter = user != null ? user.getMailAccount().substring(0, 1).toUpperCase() : null;

        Circle circle = new Circle(30);
        circle.setStroke(Color.DARKGRAY);
        circle.setFill(Color.CORNFLOWERBLUE);
        circle.setStrokeWidth(2);

        Text letter = new Text(firstLetter);
        letter.setFill(Color.WHITE);
        letter.setFont(Font.font(25));

        StackPane avatarStack = new StackPane(circle, letter);

        if (imgaeProfile != null) {
            circle.setFill(new ImagePattern(imgaeProfile));
            letter.setVisible(false);
        } else {
            circle.setFill(Color.CORNFLOWERBLUE);
            letter.setVisible(true);
        }

        this.getChildren().add(avatarStack);

    }

    public Avatar(Stage stage, UserController userController, ScreenController screenController) {

        UserToken currUserToken = userController.getLoggedUser();
        String firstLetter = currUserToken != null ? currUserToken.getMailAccount().substring(0, 1).toUpperCase()
                : null;

        Popup dropdown = new Popup();
        VBox dropdownContent = new VBox(10);

        Circle circle = new Circle(20);
        circle.setStroke(Color.DARKGRAY);
        circle.setFill(Color.CORNFLOWERBLUE);
        circle.setStrokeWidth(2);

        Text letter = new Text(firstLetter);
        letter.setFill(Color.WHITE);
        letter.setFont(Font.font(20));

        Button switchUserButton = new Button("Switch user");
        switchUserButton.setOnAction(e -> {
            dropdown.hide();
            screenController.activate("switchUser", stage);
        });

        Button addAnotherAccountButton = new Button("Add acount");
        addAnotherAccountButton.setOnAction(e -> {
            dropdown.hide();
            screenController.activate("addAnotherAccount", stage);
        });

        Button updateProfileImageButton = new Button("Update profile image");
        updateProfileImageButton.setOnAction(e -> {
            dropdown.hide();
            screenController.activate("updateAvatarImage", stage);
        });

        Button resetPasswordButton = new Button("Resest password");
        resetPasswordButton.setOnAction(e -> {
            dropdown.hide();
            screenController.activate("reset", stage);
        });

        dropdownContent.setStyle(
                "-fx-background-color: whitesmoke; -fx-border-color: #D8DAC2; -fx-border-radius: 10; -fx-background-radius: 10;");
        dropdownContent.setPadding(new Insets(10));

        dropdownContent.getChildren().addAll(addAnotherAccountButton, updateProfileImageButton, switchUserButton,
                resetPasswordButton);
        dropdownContent.setAlignment(Pos.CENTER);
        dropdown.getContent().add(dropdownContent);

        StackPane avatarStack = new StackPane(circle, letter);

        avatarStack.setOnMouseClicked(e -> {
            if (dropdown.isShowing()) {
                dropdown.hide();
            } else {
                Platform.runLater(() -> {
                    Bounds avatarBounds = avatarStack.localToScreen(avatarStack.getBoundsInLocal());
                    double x = avatarBounds.getMinX() + (avatarBounds.getWidth() / 2)
                            - (dropdownContent.getWidth() / 2);
                    double y = avatarBounds.getMaxY() + 10;

                    dropdown.show(avatarStack, x, y);
                });
            }
        });

        StateEventService.getInstance().subscribe("updateAvatar", playload -> {
            if (playload == null || playload instanceof Image) {
                updateAvatarImage(circle, (Image) playload, letter);
            }
        });

        this.getChildren().add(avatarStack);
    }

    private void updateAvatarImage(Circle circle, Image newImage, Text letter) {
        if (newImage != null) {
            circle.setFill(new ImagePattern(newImage));
            letter.setVisible(false);
        } else {
            circle.setFill(Color.CORNFLOWERBLUE);
            letter.setVisible(true);
        }
    }

    public void setImage(Image newImage) {
        StateEventService.getInstance().emit("updateAvatar", newImage);
    }
}
