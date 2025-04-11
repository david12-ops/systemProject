package com.example.constroller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Message;
import com.example.model.MessageModel;
import com.example.model.UserToken;
import com.example.utils.enums.MessageType;

public class MessageController {
    private MessageModel model;

    public MessageController(MessageModel model) {
        this.model = model;
    }

    public void sendMessage(String subject, String message, String senderId, String recevierId) {
        Message newMessage = new Message(null, senderId, recevierId, subject, message, LocalDateTime.now());
        model.addMesssage(newMessage);
    }

    public void responseToMessage() {
        throw new UnsupportedOperationException("Unimplemented method 'responseToMessage'");
    }

    public void removeMessage(Message message) {
        model.removeMessage(message);
    }

    public List<Message> getMessages(MessageType type, UserToken userToken) {
        List<Message> messages = new ArrayList<>();

        if (type == MessageType.SENDED && userToken != null) {
            messages = model.getAllSendedMessagesByUser(userToken.getUserId());
        }

        if (type == MessageType.RECEVIED && userToken != null) {
            messages = model.getAllReceviedMessagesByUser(userToken.getUserId());
        }

        return messages;
    }

}
