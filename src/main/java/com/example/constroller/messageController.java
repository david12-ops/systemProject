package com.example.constroller;

import com.example.model.Message;
import com.example.model.MessagesModel;

public class messageController {
    private MessagesModel model = new MessagesModel();

    public void addMesssage(String subject, String description, String sender, String acceptor) {
        Message newMessage = new Message(subject, description, sender, acceptor);
        model.addMesssage(newMessage);
    }

}
