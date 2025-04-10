package com.example.components;

import com.example.constroller.ScreenController;
import com.example.constroller.UserController;
import com.example.view.ForgotCredentialsScreen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AppBar extends HBox {
    private final Label title;

    private final Button logOutButton;

    public AppBar(String appTitle) {
        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setStyle("-fx-background-color: #2C3E50;");

        title = new Label(appTitle);
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        logOutButton = new Button("Logout");
        // logOutButton.getStyleClass().add("button");
        this.getChildren().addAll(title, spacer, logOutButton);
    }

    public Button getLogoutButton() {
        return logOutButton;
    }

    public void setTitle(String newTitle) {
        title.setText(newTitle);
    }
}
