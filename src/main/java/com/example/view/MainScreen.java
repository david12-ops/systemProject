package com.example.view;

import com.example.constroller.ScreenController;
import com.example.constroller.UserController;
import com.example.model.Message;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainScreen extends VBox {

    public MainScreen(Stage stage, ScreenController screenController, UserController userController) {

        // messageListView.setCellFactory(lv -> new ListCell<Message>() {
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

        ObservableList<String> items = FXCollections.observableArrayList("One", "Two", "Three");
        ListView<String> listView = new ListView(items);

        VBox list = new VBox(listView);
        list.setAlignment(Pos.CENTER);

        this.getChildren().add(list);
        this.setAlignment(Pos.CENTER);
    }

    public static void show(Stage primaryStage, ScreenController screenController, UserController userController) {
        Scene scene = new Scene(new MainScreen(primaryStage, screenController, userController));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
