package com.example.view;

import com.example.components.AppBar;
import com.example.components.Avatar;
import com.example.components.SideBar;
import com.example.constroller.ScreenController;
import com.example.constroller.UserController;
import com.example.utils.enums.AvatarCompPosition;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SwitchUserScreen extends VBox {

    public SwitchUserScreen(Stage stage, ScreenController screenController, UserController userController) {

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

        TilePane tilePane = new TilePane();
        tilePane.setPrefColumns(4);
        tilePane.setHgap(10);
        tilePane.setVgap(10);
        tilePane.setPadding(new Insets(10));

        for (int i = 0; i < 20; i++) {

            Avatar avatar = new Avatar(stage, userController, screenController, 30, AvatarCompPosition.TILEPANE);
            Button switchUser = new Button("switch");
            switchUser.setOnAction(e -> {
                System.out.println("switch");
                screenController.activate("main", stage);
            });

            VBox box = new VBox(20, avatar, switchUser);
            box.setPadding(new Insets(20));
            box.setStyle(
                    "-fx-background-color: whitesmoke; -fx-border-color: #D8DAC2; -fx-border-radius: 10; -fx-background-radius: 10;");

            tilePane.getChildren().addAll(box);
        }

        HBox mainContent = new HBox(sideBar, tilePane);
        mainContent.setAlignment(Pos.TOP_LEFT);

        this.getChildren().addAll(appBar, mainContent);
        this.setSpacing(0);
    }

    public static void show(Stage stage, ScreenController screenController, UserController userController) {
        Scene scene = new Scene(new AddAnotherAccountScreen(stage, screenController, userController));
        stage.setScene(scene);
        stage.show();
    }

}
