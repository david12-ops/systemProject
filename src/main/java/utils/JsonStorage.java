package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public abstract class JsonStorage<T> {

    private final Path filePath;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TypeReference<List<T>> typeReference;

    protected List<T> items;

    public JsonStorage(String fileName, TypeReference<List<T>> typeReference) {
        this.filePath = Path.of(System.getProperty("user.home"), fileName);
        this.typeReference = typeReference;
        loadFromFile();
    }

    protected void loadFromFile() {
        File file = filePath.toFile();
        if (file.exists()) {
            try {
                items = objectMapper.readValue(file, typeReference);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            items = createEmptyList();
            saveToFile();
        }
    }

    protected void saveToFile() {
        try {
            objectMapper.writeValue(filePath.toFile(), items);
            System.out.println("Data saved to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<T> getItems() {
        return items;
    }

    public void addItem(T item) {
        items.add(item);
        saveToFile();
    }

    public void removeItem(T item) {
        items.remove(item);
        saveToFile();
    }

    public void updateItem(T item, T updatedItem) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(item)) {
                items.set(i, updatedItem);
                return;
            }
        }
        saveToFile();
    }

    protected abstract List<T> createEmptyList();

}
