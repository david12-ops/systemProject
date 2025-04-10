package com.example.view;

import com.example.components.AppBar;
import com.example.constroller.MessageController;
import com.example.constroller.ScreenController;
import com.example.constroller.UserController;
import com.example.model.Message;
import com.example.model.UserToken;
import com.example.utils.enums.MessageType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class MainScreen extends BorderPane {

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

        AppBar appBar = new AppBar("Send It!");
        appBar.getLogoutButton().setOnAction(event -> {
            userController.logOut();
            screenController.updateScreen("reset",
                    new ForgotCredentialsScreen(stage, screenController, userController));
            screenController.activate("login", stage);
        });

        this.setTop(appBar);
        this.setCenter(listView);
    }

    public static void show(Stage primaryStage, ScreenController screenController, UserController userController,
            MessageController messageController) {
        Scene scene = new Scene(new MainScreen(primaryStage, screenController, userController, messageController));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
