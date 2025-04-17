package com.example.view;

import com.example.components.AppBar;
import com.example.components.Avatar;
import com.example.components.SideBar;
import com.example.constroller.MessageController;
import com.example.constroller.ScreenController;
import com.example.constroller.UserController;
import com.example.model.Message;
import com.example.model.UserToken;
import com.example.utils.SessionHolder;
import com.example.utils.enums.MessageType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends VBox {
    // TODO - styling
    // TODO - style switch user page, do functions to switch user
    // account

    public MainScreen(Stage stage, ScreenController screenController, UserController userController,
            MessageController messageController) {
        UserToken userToken = userController.getLoggedUser();

        List<Message> receviedMessages = messageController.getMessages(MessageType.RECEVIED, userToken);

        // listView.setCellFactory(lv -> new ListCell<>() {
        // @Override
        // protected void updateItem(Message msg, boolean empty) {
        // super.updateItem(msg, empty);
        // if (empty || msg == null) {
        // setText(null);
        // } else {
        // setText(msg.getSender() + " - " + msg.getSubject());
        // }
        // }
        // });

        ObservableList<Message> items = FXCollections.observableList(receviedMessages);
        ListView<String> listView = new ListView(items);

        AppBar appBar = new AppBar(stage, "Send It!", userController, screenController);
        appBar.setAvatarImage(userController.getImageProfile());
        appBar.getLogoutButton().setOnAction(event -> {
            userController.logOut();
            screenController.updateScreen("reset",
                    new ForgotCredentialsScreen(stage, screenController, userController));
            screenController.activate("login", stage);
        });

        SideBar sideBar = new SideBar();

        appBar.getBurgerButton().setOnAction(e -> {
            if (sideBar.isVisible()) {
                sideBar.setVisible(false);
                sideBar.setManaged(false);
            } else {
                sideBar.setVisible(true);
                sideBar.setManaged(true);
            }
        });

        Button button = new Button("Update profile");
        button.setOnAction(event -> {
            screenController.activate("updateAvatarImage", stage);
        });

        VBox contentBox = new VBox(20, button, listView);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setSpacing(20);

        HBox mainContent = new HBox(sideBar, contentBox);
        mainContent.setAlignment(Pos.TOP_LEFT);

        this.getChildren().addAll(appBar, mainContent);
        this.setSpacing(0);

    }

    public static void show(Stage stage, ScreenController screenController, UserController userController,
            MessageController messageController) {
        Scene scene = new Scene(new MainScreen(stage, screenController, userController, messageController));
        scene.setFill(Color.WHITE);
        stage.setScene(scene);
        stage.show();
    }

}
