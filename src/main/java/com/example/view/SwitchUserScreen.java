package com.example.view;

import java.util.List;

import com.example.components.Avatar;
import com.example.components.Layout;
import com.example.constroller.MessageController;
import com.example.constroller.ScreenController;
import com.example.constroller.UserController;
import com.example.model.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SwitchUserScreen extends VBox {

    private void updateScreens(ScreenController screenController, UserController userController,
            MessageController messageController, Stage stage) {
        screenController.updateScreen("main",
                new MainScreen(stage, screenController, userController, messageController));
        screenController.updateScreen("reset", new ForgotCredentialsScreen(stage, screenController, userController));
        screenController.updateScreen("switchUser",
                new SwitchUserScreen(stage, screenController, userController, messageController));
    }

    public SwitchUserScreen(Stage stage, ScreenController screenController, UserController userController,
            MessageController messageController) {

        List<User> users = userController.getAllUserAccounts();
        Layout layout = null;
        VBox content = null;

        Text textInfo = new Text("No another accounts found");
        textInfo.setFill(Color.ORANGERED);
        textInfo.setFont(Font.font(30));

        Text textTitle = new Text("Your accounts");
        textTitle.setFill(Color.web("rgb(244, 160, 4)"));
        textTitle.setFont(Font.font(30));

        if (users.size() > 0) {
            TilePane tilePane = new TilePane();
            tilePane.setPrefColumns(4);
            tilePane.setHgap(10);
            tilePane.setVgap(10);
            tilePane.setPadding(new Insets(10));

            for (int i = 0; i < users.size(); i++) {
                int index = i;
                Avatar avatar = new Avatar(stage, users.get(index));

                Text textEm = new Text(users.get(index).getMailAccount());
                textEm.setFont(Font.font("Arial", FontWeight.BOLD, 18));

                Button switchUserButton = new Button("switch");
                switchUserButton.setOnAction(e -> {
                    boolean switched = userController.switchAccount(users.get(index));
                    if (switched) {
                        updateScreens(screenController, userController, messageController, stage);
                        screenController.activate("main", stage);
                    }
                });

                Button removeAccountButton = new Button("remove");
                removeAccountButton.setOnAction(e -> {
                    userController.removeAccount(users.get(index));
                    screenController.updateScreen("switchUser",
                            new SwitchUserScreen(stage, screenController, userController, messageController));
                    screenController.activate("main", stage);
                });

                HBox boxButton = new HBox(10, removeAccountButton, switchUserButton);
                boxButton.setAlignment(Pos.CENTER);

                VBox box = new VBox(20, avatar, textEm, boxButton);
                box.setAlignment(Pos.CENTER);
                box.setPadding(new Insets(30));
                box.setStyle(
                        "-fx-background-color: whitesmoke; -fx-border-color: #D8DAC2; -fx-border-radius: 10; -fx-background-radius: 10;");

                tilePane.getChildren().addAll(box);
            }

            content = new VBox(textTitle, tilePane);

            layout = new Layout(stage, content, screenController, userController, messageController);
        } else {

            content = new VBox(textTitle, textInfo);

            layout = new Layout(stage, content, screenController, userController, messageController);
        }

        this.getChildren().add(layout);
    }

    public static void show(Stage stage, ScreenController screenController, UserController userController,
            MessageController messageController) {
        Scene scene = new Scene(new SwitchUserScreen(stage, screenController, userController, messageController));
        stage.setScene(scene);
        stage.show();
    }

}
