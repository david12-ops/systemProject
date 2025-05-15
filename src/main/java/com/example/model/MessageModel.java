package com.example.model;

import com.example.utils.ErrorToolManager;
import com.example.utils.JsonStorageTool;
import com.example.utils.services.ValidationService;
import com.fasterxml.jackson.core.type.TypeReference;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.HashMap;
import java.util.List;

public class MessageModel {

    static Dotenv dotenv = Dotenv.load();
    private HashMap<String, String> errorMap = new HashMap<>();
    private final ErrorToolManager errorToolManager = new ErrorToolManager(errorMap);
    private final ValidationService validationService = new ValidationService();
    private final ValidationService.MessageModelValidations validator = validationService.new MessageModelValidations(
            errorToolManager);
    private List<Message> listOfMessages;
    private JsonStorageTool<Message> storageTool;

    public MessageModel() {
        storageTool = new JsonStorageTool<Message>(dotenv.get("FILE_PATH_MESSAGES"),
                new TypeReference<List<Message>>() {
                });
        this.listOfMessages = storageTool.getItems();
    }

    public void addMesssage(Message message) {
        storageTool.addItem(message);
    }

    public void removeMessage(Message message) {
        storageTool.removeItem(message);
    }

    public List<Message> getAllReceviedMessagesByUser(String userId) {
        return listOfMessages.stream().filter(message -> message.getReceiverId().equals(userId)).toList();
    }

    public List<Message> getAllSendedMessagesByUser(String userId) {
        return listOfMessages.stream().filter(message -> message.getSenderId().equals(userId)).toList();
    }
}
