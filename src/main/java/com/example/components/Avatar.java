package com.example.components;

import com.example.constroller.UserController;
import com.example.model.UserToken;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Avatar extends StackPane {
    private final Circle circle;
    private final String firstLetter;
    private final ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();

    public Avatar(UserController userController) {

        UserToken currUserToken = userController.getLoggedUser();
        firstLetter = currUserToken != null ? currUserToken.getMailAccount().substring(0, 1).toUpperCase() : null;

        circle = new Circle(20);
        circle.setStroke(Color.DARKGRAY);
        circle.setFill(Color.CORNFLOWERBLUE);
        circle.setStrokeWidth(2);

        Text letter = new Text(firstLetter);
        letter.setFill(Color.WHITE);
        letter.setFont(Font.font(20));
        this.getChildren().addAll(circle, letter);

        imageProperty.addListener((observable, oldImage, newImage) -> {
            updateAvatarImage(newImage, letter);
        });
    }

    private void updateAvatarImage(Image newImage, Text letter) {
        if (newImage != null) {
            circle.setFill(new ImagePattern(newImage));
            letter.setVisible(false);
        } else {
            letter.setVisible(true);
        }
    }

    public void setImage(Image newImage) {
        imageProperty.set(newImage);
    }
}
