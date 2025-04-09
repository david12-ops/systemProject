package com.example.constroller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Message;
import com.example.model.MessageModel;

import utils.Enums.MessageType;
import utils.UserToken;

public class MessageController {
    private MessageModel model;

    public MessageController(MessageModel model) {
        this.model = model;
    }

    public void addMesssage(String subject, String message, String senderId, String recevierId) {
        Message newMessage = new Message(senderId, recevierId, subject, message, LocalDateTime.now());
        model.addMesssage(newMessage);
    }

    public void removeMessage(Message message) {
        model.removeMessage(message);
    }

    public List<Message> getMessages(MessageType type, UserToken userToken) {
        List<Message> messages = new ArrayList<>();

        if (type == MessageType.SENDED && userToken != null) {
            messages = model.getAllSendedMessagesByUser(userToken.getId());
        }

        if (type == MessageType.RECEVIED && userToken != null) {
            messages = model.getAllReceviedMessagesByUser(userToken.getId());
        }

        return messages;
    }

}
