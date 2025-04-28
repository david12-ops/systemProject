package com.example.view;

import com.example.components.AppBar;
import com.example.components.Avatar;
import com.example.components.Layout;
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

import javax.swing.UIDefaults.LazyInputMap;

public class MainScreen extends VBox {
    // TODO - styling
    // TODO - style switch user page
    // TODO - avatar image update bug
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

        Button button = new Button("Update profile");
        button.setOnAction(event -> {
            screenController.activate("updateAvatarImage", stage);
        });

        VBox mainBox = new VBox(button);

        Layout layout = new Layout(stage, mainBox, screenController, userController, messageController);

        this.getChildren().add(layout);

    }

    public static void show(Stage stage, ScreenController screenController, UserController userController,
            MessageController messageController) {
        Scene scene = new Scene(new MainScreen(stage, screenController, userController, messageController));
        stage.setScene(scene);
        stage.show();
    }

}
