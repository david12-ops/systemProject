package com.example.components;

import com.example.constroller.MessageController;
import com.example.constroller.ScreenController;
import com.example.constroller.UserController;
import com.example.view.ForgotCredentialsScreen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Layout extends VBox {

    public Layout(Stage stage, Node content, ScreenController screenController, UserController userController,
            MessageController messageController) {

        VBox contentArea = new VBox(content);
        contentArea.setPadding(new Insets(20));
        contentArea.setFillWidth(false);
        contentArea.setAlignment(Pos.CENTER);

        AppBar appBar = new AppBar(stage, "Send It!", userController, screenController);
        appBar.setAvatarImage(userController.getImageProfile());
        appBar.getLogoutButton().setOnAction(event -> {
            userController.logOut();
            screenController.updateScreen("reset",
                    new ForgotCredentialsScreen(stage, screenController, userController));
            screenController.activate("login", stage);
        });

        SideBar sideBar = new SideBar(stage, screenController);
        sideBar.setVisible(false);
        sideBar.setManaged(false);

        appBar.getBurgerButton().setOnAction(e -> {
            boolean show = !sideBar.isVisible();
            sideBar.setVisible(show);
            sideBar.setManaged(show);

            contentArea.setAlignment(show ? Pos.CENTER_LEFT : Pos.CENTER);
        });

        BorderPane root = new BorderPane();
        root.setTop(appBar);
        root.setLeft(sideBar);
        root.setCenter(contentArea);
        VBox.setVgrow(root, Priority.ALWAYS);
        this.getChildren().add(root);
    }

}
