package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("description")
    private String description;

    @JsonProperty("sender")
    private String sender;

    @JsonProperty("acceptor")
    private String acceptor;

    private Message message;

    public Message(String subject, String description, String sender, String acceptor) {
        this.subject = subject;
        this.description = description;
        this.sender = sender;
        this.acceptor = acceptor;
        this.message = createMessage(subject, description, sender, acceptor);
    }

    public String getSender() {
        return message.sender;
    }

    public String getAcceptor() {
        return message.acceptor;
    }

    public String getSubject() {
        return message.subject;
    }

    public String getDescription() {
        return message.description;
    }

    protected Message createMessage(String title, String description, String fromWho, String toUser) {
        Message message = new Message(title, description, fromWho, toUser);
        return message;
    }

    @Override
    public String toString() {
        return "'" + subject + "'" + "\n" + "'" + description + "'" + "\n" + "'" + sender + "\n" + acceptor;
    }
}
