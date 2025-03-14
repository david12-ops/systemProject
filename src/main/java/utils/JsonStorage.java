package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class JsonStorage<T> {

    Dotenv dotenv = Dotenv.load();
    private final String FOLDER_PATH = dotenv.get("FOLDER_DATA_LOCATION");

    private final Path filePath;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TypeReference<List<T>> typeReference;
    private List<T> items;

    public JsonStorage(String fileName, TypeReference<List<T>> typeReference) {
        this.filePath = Path.of(FOLDER_PATH, fileName);
        this.typeReference = typeReference;
        loadFromFile();
    }

    private void loadFromFile() {
        File file = filePath.toFile();
        if (file.exists() && file.length() > 0) {
            try {
                this.items = objectMapper.readValue(file, typeReference);
            } catch (IOException e) {
                e.printStackTrace();
                this.items = new ArrayList<>();
            }
        } else {
            this.items = createEmptyList();
            saveToFile();
        }
    }

    private void saveToFile() {
        try {
            System.out.println("items: " + this.items);
            objectMapper.writeValue(filePath.toFile(), this.items);
            System.out.println("Data saved to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected List<T> getItems() {
        return this.items;
    }

    protected void addItem(T item) {
        this.items.add(item);
        saveToFile();
    }

    protected void removeItem(T item) {
        this.items.remove(item);
        saveToFile();
    }

    protected void updateItem(T item, T updatedItem) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(item)) {
                this.items.set(i, updatedItem);
                break;
            }
        }
        saveToFile();
    }

    protected abstract List<T> createEmptyList();

}
