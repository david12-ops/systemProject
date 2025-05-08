package com.example.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.util.function.Consumer;

public class FileDropZone extends StackPane {

    private final ImageView imageView = new ImageView();
    private Consumer<Image> onImageDropped;

    public FileDropZone(String acceptedFile) {
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
                if (file.getName().matches(acceptedFile)) {
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

    public void setOnImageDropped(Consumer<Image> handler) {
        this.onImageDropped = handler;
    }

    public void clear() {
        setImage(null);
        imageView.setVisible(false);
    }
}
