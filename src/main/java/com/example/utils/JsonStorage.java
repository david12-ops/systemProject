package com.example.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class JsonStorage<T> {

    Dotenv dotenv = Dotenv.load();
    private final String FOLDER_PATH = dotenv.get("FOLDER_DATA_LOCATION");

    private final Path filePath;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TypeReference<List<T>> typeReference;
    // syncronized CRUD operations for multi acces - prevent race conditions
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

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
        rwLock.readLock().lock();
        try {
            return this.items;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    protected void addItem(T item) {
        rwLock.writeLock().lock();
        try {
            this.items.add(item);
            saveToFile();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    protected void removeItem(T item) {
        rwLock.writeLock().lock();
        try {
            this.items.remove(item);
            saveToFile();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    protected void updateItem(T item, T updatedItem) {
        rwLock.writeLock().lock();
        try {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).equals(item)) {
                    this.items.set(i, updatedItem);
                    break;
                }
            }
            saveToFile();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    protected abstract List<T> createEmptyList();

}
