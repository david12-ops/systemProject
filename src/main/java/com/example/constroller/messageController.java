package com.example.constroller;

import java.util.List;
import java.util.Map;

import com.example.model.Message;
import com.example.model.MessagesModel;

public class messageController {
    private MessagesModel model = new MessagesModel();

    public void addMesssage(String subject, String description, String sender, String acceptor) {
        Message newMessage = new Message(subject, description, sender, acceptor);
        model.addMesssage(newMessage);
    }

    // remove, update

    public List<Map.Entry<String, String>> getInputErrors() {
        return model.getErrors();
    }

}
