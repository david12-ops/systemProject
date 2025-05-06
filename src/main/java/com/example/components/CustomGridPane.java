package com.example.components;

import java.util.ArrayList;
import java.util.List;

import com.example.controller.MessageController;
import com.example.controller.ScreenController;
import com.example.controller.UserController;
import com.example.model.User;
import com.example.utils.enums.LayoutMode;
import com.example.utils.services.StateEventService;
import com.example.view.ForgotCredentialsScreen;
import com.example.view.MainScreen;
import com.example.view.SwitchUserScreen;

import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class CustomGridPane extends HBox {

    private class GridDimension {

        private Integer rows;
        private Integer columns;

        public GridDimension(int rows, int columns) {
            this.rows = rows;
            this.columns = columns;
        }

        public Integer getRows() {
            return rows;
        }

        public Integer getColumns() {
            return columns;
        }
    }

    private List<List<User>> userLists;
    private List<GridPane> gridPanes;
    private Integer page = 0;
    private Integer totalPages;

    private VBox buttonBox = new VBox(20);
    private HBox contentBox = new HBox(20);

    private LayoutMode getLayoutMode(double stageWidth) {
        if (stageWidth < 950) {
            return LayoutMode.MOBILE;
        } else if (stageWidth >= 950 && stageWidth < 1210) {
            return LayoutMode.TABLET;
        } else {
            return LayoutMode.DESKTOP;
        }
    }

    private GridDimension getGridDimension(double stageWidth) {
        LayoutMode mode = getLayoutMode(stageWidth);

        switch (mode) {
        case MOBILE:
            return new GridDimension(2, 1);
        case TABLET:
            return new GridDimension(2, 2);
        case DESKTOP:
            return new GridDimension(2, 3);
        default:
            return new GridDimension(2, 3);
        }
    }

    private void refreshLayout(UserController userController, ScreenController screenController,
            MessageController messageController, Stage stage) {
        GridDimension gridDimension = getGridDimension(stage.getWidth());
        computeGridPanesAndList(userController.getAllUserAccounts(), gridDimension);
        if (page >= totalPages)
            page = Math.max(0, totalPages - 1);
        switchGridPane(gridPanes.get(page), userLists.get(page), stage, userController, screenController,
                messageController, gridDimension.getColumns());
    }

    private void computeGridPanesAndList(List<User> users, GridDimension gridDimension) {

        int gridWidth = 0;
        int gridHeight = 0;

        int itemsPerPage = gridDimension.getColumns() * gridDimension.getRows();
        totalPages = (int) Math.ceil((double) users.size() / itemsPerPage);
        userLists = new ArrayList<>();
        gridPanes = new ArrayList<>();

        if (gridDimension.getColumns() == 3) {
            gridWidth = 870;
            gridHeight = 650;
        }

        if (gridDimension.getColumns() == 1) {
            gridWidth = 320;
            gridHeight = 650;
        }

        if (gridDimension.getColumns() == 2) {
            gridWidth = 620;
            gridHeight = 650;
        }

        for (int i = 0; i < totalPages; i++) {
            int start = i * itemsPerPage;
            int end = Math.min(start + itemsPerPage, users.size());

            GridPane grid = new GridPane();
            grid.setStyle("-fx-padding: 20 0 0 0;");

            grid.setMinHeight(gridHeight);
            grid.setMaxHeight(gridHeight);
            grid.setPrefHeight(gridHeight);

            grid.setMinWidth(gridWidth);
            grid.setMaxWidth(gridWidth);
            grid.setPrefWidth(gridWidth);

            grid.setHgap(60);
            grid.setVgap(45);

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
            ScreenController screenController, MessageController messageController, int columns) {

        for (int i = 0; i < users.size(); i++) {
            int index = i;
            int col = i % columns;
            int row = i / columns;
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
                boolean removed = userController.removeAccount(users.get(index));
                if (removed) {
                    if (page > 0 && userLists.size() > page && userLists.get(page).size() == 1) {
                        page = page - 1;
                    }
                    StateEventService.getInstance().emit("computeGrid", userController.getAllUserAccounts());
                }
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

    public CustomGridPane(ScreenController screenController, UserController userController,
            MessageController messageController, Stage stage) {

        Button nextButton = new Button(">");
        nextButton.setShape(new Circle(20));
        nextButton.setMinSize(40, 40);
        nextButton.setMaxSize(40, 40);
        nextButton.getStyleClass().add("circleButton");
        nextButton.setOnAction(e -> {
            if (page < totalPages - 1) {
                GridDimension gridDimension = getGridDimension(stage.getWidth());
                page = page + 1;
                if (page < gridPanes.size() && page < userLists.size()) {
                    switchGridPane(gridPanes.get(page), userLists.get(page), stage, userController, screenController,
                            messageController, gridDimension.getColumns());
                } else {
                    System.err.println("Failed to load user accounts");
                }
            }
        });

        Button prevButton = new Button("<");
        prevButton.setShape(new Circle(20));
        prevButton.setMinSize(40, 40);
        prevButton.setMaxSize(40, 40);
        prevButton.getStyleClass().add("circleButton");
        prevButton.setOnAction(e -> {
            if (page > 0) {
                GridDimension gridDimension = getGridDimension(stage.getWidth());
                page = page - 1;
                if (page < gridPanes.size() && page < userLists.size()) {
                    switchGridPane(gridPanes.get(page), userLists.get(page), stage, userController, screenController,
                            messageController, gridDimension.getColumns());
                } else {
                    System.err.println("Failed to load user accounts");
                }
            }
        });

        buttonBox = new VBox(10, prevButton, nextButton);
        buttonBox.setAlignment(Pos.CENTER);

        PauseTransition resizeDelay = new PauseTransition(Duration.millis(300));
        resizeDelay.setOnFinished(e -> refreshLayout(userController, screenController, messageController, stage));

        StateEventService.getInstance().subscribe("computeGrid",
                payload -> refreshLayout(userController, screenController, messageController, stage));

        stage.widthProperty().addListener((obs, oldVal, newVal) -> resizeDelay.playFromStart());

        StateEventService.getInstance().emit("computeGrid", userController.getAllUserAccounts());
        this.getChildren().setAll(contentBox);
    }
}
