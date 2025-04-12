package com.example.components;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.function.Consumer;

public class ImageDropZone extends StackPane {

    private final ImageView imageView = new ImageView();

    public ImageDropZone(Consumer<Image> onImageDropped) {
        setPrefSize(300, 200);
        setStyle("-fx-border-color: #999; -fx-border-width: 2; -fx-border-style: dashed; -fx-alignment: center;");

        imageView.setPreserveRatio(true);
        imageView.setFitWidth(200);
        getChildren().add(imageView);

        // Drag over
        setOnDragOver(event -> {
            if (event.getGestureSource() != this && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        // Drop
        setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasFiles()) {
                File file = db.getFiles().get(0);
                if (file.getName().matches(".*\\.(png|jpg|jpeg|gif)")) {
                    Image image = new Image(file.toURI().toString());
                    imageView.setImage(image);
                    if (onImageDropped != null) {
                        onImageDropped.accept(image);
                    }
                    success = true;
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    public Image getImage() {
        return imageView.getImage();
    }

    public void setImage(Image image) {
        imageView.setImage(image);
    }

    // MenuBar menuBar = new MenuBar();
    // Menu fileMenu = new Menu("File");
    // MenuItem uploadItem = new MenuItem("Upload Image");

    // // File chooser logic
    // uploadItem.setOnAction(e -> {
    // FileChooser fileChooser = new FileChooser();
    // fileChooser.setTitle("Select Image");
    // fileChooser.getExtensionFilters().addAll(
    // new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg",
    // "*.gif")
    // );
    // File selectedFile = fileChooser.showOpenDialog(stage);
    // if (selectedFile != null) {
    // Image image = new Image(selectedFile.toURI().toString());
    // // show in ImageView or attach to a message, etc.
    // }
    // });

    // fileMenu.getItems().add(uploadItem);
    // menuBar.getMenus().add(fileMenu);

    // VBox topContainer = new VBox(appBar, menuBar);
    // this.setTop(topContainer);
}
