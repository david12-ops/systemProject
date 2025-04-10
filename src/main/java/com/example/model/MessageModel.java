package com.example.model;

import com.example.utils.JsonStorage;
import com.example.utils.services.AplicationService;
import com.fasterxml.jackson.core.type.TypeReference;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageModel extends JsonStorage<Message> {

    static Dotenv dotenv = Dotenv.load();

    private HashMap<String, String> errorMap = new HashMap<>();
    AplicationService service = AplicationService.getInstance(this.errorMap);
    private List<Message> listOfMessages;

    public MessageModel() {
        super(dotenv.get("FILE_PATH_MESSAGES"), new TypeReference<List<Message>>() {
        });
        this.listOfMessages = getItems();
    }

    public void addMesssage(Message message) {
        addItem(message);
    }

    public void removeMessage(Message message) {
        removeItem(message);
    }

    public List<Message> getAllReceviedMessagesByUser(String userId) {
        return listOfMessages.stream().filter(message -> message.getReceiverId().equals(userId)).toList();
    }

    public List<Message> getAllSendedMessagesByUser(String userId) {
        return listOfMessages.stream().filter(message -> message.getSenderId().equals(userId)).toList();
    }

    @Override
    protected List<Message> createEmptyList() {
        listOfMessages = new ArrayList<>();
        return listOfMessages;
    }

}
