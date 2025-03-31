package com.example.constroller;

import java.util.HashMap;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ScreenController {
    private final HashMap<String, Pane> screenMap = new HashMap<>();
    private Scene main;

    public ScreenController(Scene main) {
        this.main = main;
    }

    public void setScene(Scene scene) {
        this.main = scene;
    }

    public void addScreen(String name, Pane pane) {
        screenMap.put(name, pane);
    }

    public void removeScreen(String name) {
        if (!screenMap.containsKey(name)) {
            System.out.println("Attempted to remove non-existing screen: " + name);
            return;
        }
        screenMap.remove(name);
    }

    public void updateScreen(String name, Pane newPane) {
        if (screenMap.containsKey(name)) {
            screenMap.put(name, newPane);
        } else {
            throw new IllegalArgumentException("Screen not found: " + name);
        }
    }

    public void activate(String name, Stage stage) {
        if (main == null) {
            throw new IllegalStateException("Scene has not been set for ScreenController.");
        }

        Pane screen = screenMap.get(name);
        if (screen != null) {
            main.setRoot(screen);

            switch (name) {
            case "login":
                stage.setTitle("Log in");
                stage.setWidth(500);
                stage.setHeight(550);
                break;
            case "register":
                stage.setTitle("Registration");
                stage.setWidth(500);
                stage.setHeight(400);
                break;
            case "reset":
                stage.setTitle("Reset Password");
                stage.setWidth(500);
                stage.setHeight(650);
                break;
            default:
                stage.setTitle("Application");
            }
        } else {
            throw new IllegalArgumentException("Screen not found: " + name);
        }
    }

}
