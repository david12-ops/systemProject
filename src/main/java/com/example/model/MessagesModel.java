package com.example.model;

import com.fasterxml.jackson.core.type.TypeReference;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utils.AplicationService;
import utils.JsonStorage;

public class MessagesModel extends JsonStorage<Message> {

    static Dotenv dotenv = Dotenv.load();

    private List<Map.Entry<String, String>> errorList = new ArrayList<>();
    AplicationService service = AplicationService.getInstance(this.errorList);
    private List<Message> listOfMessages;

    public MessagesModel() {
        super(dotenv.get("FILE_PATH_MESSAGES"), new TypeReference<List<Message>>() {
        });
        this.listOfMessages = getItems();
    }

    // public void addMesssage(Message message) {
    // validateData(message.getSender(), message.getAcceptor());
    // if (service.getErrHandler().getSizeErrorList() == 0) {
    // addItem(message);
    // service.getErrHandler().clearErrorList();
    // }
    // }

    // public void removeMessage(Message message) {
    // removeItem(message);
    // }

    // protected void validateData(String sender, String acceptor) {
    // service.getValidationHandler().validateUserData(sender, acceptor);
    // }

    // public List<Message> getAllMesagesByUser(String senderUserEm) {
    // return getItems().stream().filter(message ->
    // message.getSender().equals(senderUserEm)).toList();
    // }

    @Override
    protected List<Message> createEmptyList() {
        this.listOfMessages = new ArrayList<>();
        return listOfMessages;
    }

}
