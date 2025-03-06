package com.example.model;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import utilities.JsonStorage;

public class MessagesModel extends JsonStorage<Message> {
    private static final String FILE_PATH = "/messages.json";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    // Error Mnager ?
    private List<Map.Entry<String, String>> errorList = new ArrayList<>();

    public MessagesModel() {
        super(FILE_PATH, new TypeReference<List<Message>>() {
        });
        loadFromFile();
    }

    public void addMesssage(Message message) {
        validateData(message.getSender(), message.getAcceptor());
        if (errorList.size() == 0) {
            addItem(message);
            errorList.clear();
        }
    }

    // remove, update

    protected void validateData(String sender, String acceptor) {
        if (!Pattern.compile(EMAIL_REGEX).matcher(sender).matches()) {
            errorList.add(new AbstractMap.SimpleEntry<>("sender", "Provided sender is not in correct format"));
        }

        if (!Pattern.compile(EMAIL_REGEX).matcher(acceptor).matches()) {
            errorList.add(new AbstractMap.SimpleEntry<>("acceptor", "Provided acceptor is not in correct format"));
        }
    }

    public List<Map.Entry<String, String>> getErrors() {
        return errorList;
    }

    public List<Message> getAllMesagesByUser(String senderUserEm) {
        return getItems().stream().filter(message -> message.getSender().equals(senderUserEm)).toList();
    }

    @Override
    protected List<Message> createEmptyList() {
        List<Message> listOfMessages = new ArrayList<>();
        return listOfMessages;
    }

}
