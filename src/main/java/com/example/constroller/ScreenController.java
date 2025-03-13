package com.example.constroller;

import java.util.HashMap;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

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

    public void activate(String name) {
        if (main == null) {
            throw new IllegalStateException("Scene has not been set for ScreenController.");
        }

        Pane pane = screenMap.get(name);
        if (pane != null) {
            main.setRoot(pane);
        } else {
            throw new IllegalArgumentException("Screen not found: " + name);
        }
    }
}
