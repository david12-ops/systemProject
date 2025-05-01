package com.example.components;

import java.util.ArrayList;
import java.util.List;

import com.example.constroller.MessageController;
import com.example.constroller.ScreenController;
import com.example.constroller.UserController;
import com.example.model.User;
import com.example.utils.services.StateEventService;
import com.example.view.ForgotCredentialsScreen;
import com.example.view.MainScreen;
import com.example.view.SwitchUserScreen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class CostumeGridPane extends HBox {

    private List<List<User>> userLists;
    private List<GridPane> gridPanes;
    private Integer page = 0;
    private Integer totalPages;

    private VBox buttonBox = new VBox(20);
    private HBox contentBox = new HBox(20);

    private void computeGridPanesAndList(List<User> users) {

        totalPages = (int) Math.ceil((double) users.size() / 6);
        userLists = new ArrayList<>();
        gridPanes = new ArrayList<>();

        for (int i = 0; i < totalPages; i++) {
            int start = i * 6;
            int end = Math.min(start + 6, users.size());

            GridPane grid = new GridPane();
            grid.setPadding(new Insets(20));
            grid.setHgap(60);
            grid.setVgap(45);
            grid.setAlignment(Pos.CENTER);

            gridPanes.add(grid);
            userLists.add(users.subList(start, end));
        }
    }

    private void updateScreens(ScreenController screenController, UserController userController,
            MessageController messageController, Stage stage) {
        screenController.updateScreen("main",
                new MainScreen(stage, screenController, userController, messageController));
        screenController.updateScreen("reset", new ForgotCredentialsScreen(stage, screenController, userController));
        screenController.updateScreen("switchUser",
                new SwitchUserScreen(stage, screenController, userController, messageController));
    }

    private void switchGridPane(GridPane gridPane, List<User> users, Stage stage, UserController userController,
            ScreenController screenController, MessageController messageController) {

        for (int i = 0; i < users.size(); i++) {
            int index = i;
            int col = i % 3;
            int row = i / 3;
            Avatar avatar = new Avatar(stage, users.get(index));

            Label textEmlLabel = new Label(users.get(index).getMailAccount());
            textEmlLabel.setStyle("-fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 16px;");
            textEmlLabel.setWrapText(true);

            Button switchUserButton = new Button("switch");
            switchUserButton.getStyleClass().add("updateButton");
            switchUserButton.setOnAction(e -> {
                boolean switched = userController.switchAccount(users.get(index));
                if (switched) {
                    StateEventService.getInstance().emit("computeGrid", userController.getAllUserAccounts());
                    updateScreens(screenController, userController, messageController, stage);
                    screenController.activate("main", stage);
                }
            });

            Button removeAccountButton = new Button("remove");
            removeAccountButton.getStyleClass().add("deleteButton");
            removeAccountButton.setOnAction(e -> {
                userController.removeAccount(users.get(index));
                StateEventService.getInstance().emit("computeGrid", userController.getAllUserAccounts());

                if (userLists.get(page).isEmpty() && page > 0) {
                    page = page - 1;
                }

                switchGridPane(gridPanes.get(page), userLists.get(page), stage, userController, screenController,
                        messageController);
            });

            VBox boxButton = new VBox(10, removeAccountButton, switchUserButton);
            boxButton.setAlignment(Pos.CENTER);

            VBox box = new VBox(20, avatar, textEmlLabel, boxButton);
            box.setAlignment(Pos.CENTER);
            box.setPadding(new Insets(20));

            box.setMinWidth(250);
            box.setMaxWidth(250);
            box.setPrefWidth(250);

            box.setPrefHeight(280);
            box.setMaxHeight(280);
            box.setMinHeight(280);

            box.setStyle(
                    "-fx-background-color: whitesmoke; -fx-border-color: #D8DAC2; -fx-border-radius: 10; -fx-background-radius: 10;");

            gridPane.add(box, col, row);
        }

        contentBox.getChildren().setAll(gridPanes.get(page), buttonBox);
    }

    public CostumeGridPane(ScreenController screenController, UserController userController,
            MessageController messageController, Stage stage) {
        Button nextButton = new Button(">");
        nextButton.setShape(new Circle(20));
        nextButton.setMinSize(40, 40);
        nextButton.setMaxSize(40, 40);
        nextButton.getStyleClass().add("circleButton");
        nextButton.setOnAction(e -> {
            if (page < totalPages - 1) {
                try {
                    page = page + 1;
                    switchGridPane(gridPanes.get(page), userLists.get(page), stage, userController, screenController,
                            messageController);
                } catch (Exception ex) {
                    System.err.println("Failed load another users accounts: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        Button prevButton = new Button("<");
        prevButton.setShape(new Circle(20));
        prevButton.setMinSize(40, 40);
        prevButton.setMaxSize(40, 40);
        prevButton.getStyleClass().add("circleButton");
        prevButton.setOnAction(e -> {
            if (this.page > 0) {
                try {
                    this.page = page - 1;
                    switchGridPane(gridPanes.get(page), userLists.get(page), stage, userController, screenController,
                            messageController);
                } catch (Exception ex) {
                    System.err.println("Failed load another users accounts: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        buttonBox = new VBox(10, prevButton, nextButton);
        buttonBox.setAlignment(Pos.CENTER);

        StateEventService.getInstance().subscribe("computeGrid", playload -> {
            computeGridPanesAndList(userController.getAllUserAccounts());
            if (page >= totalPages)
                page = Math.max(0, totalPages - 1);
            switchGridPane(gridPanes.get(page), userLists.get(page), stage, userController, screenController,
                    messageController);
        });

        StateEventService.getInstance().emit("computeGrid", userController.getAllUserAccounts());
        this.getChildren().setAll(contentBox);
    }
}
