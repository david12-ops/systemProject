package com.example.model;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utils.AplicationService;
import utils.JsonStorage;

public class MessagesModel extends JsonStorage<Message> {
    private static final String FILE_PATH = "/messages.json";

    private List<Map.Entry<String, String>> errorList = new ArrayList<>();
    AplicationService service = AplicationService.getInstance(this.errorList);

    public MessagesModel() {
        super(FILE_PATH, new TypeReference<List<Message>>() {
        });
        loadFromFile();
    }

    public void addMesssage(Message message) {
        validateData(message.getSender(), message.getAcceptor());
        if (service.getErrHandler().getSizeErrorList() == 0) {
            addItem(message);
            service.getErrHandler().clearErrorList();
        }
    }

    public void removeMessage(Message message) {
        removeItem(message);
    }

    protected void validateData(String sender, String acceptor) {
        service.getValidationHandler().validateUserData(sender, acceptor);
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
