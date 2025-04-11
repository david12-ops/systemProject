package com.example.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {

    @JsonProperty("message_id")
    private final String messageId;

    @JsonProperty("sender_id")
    private final String senderId;

    @JsonProperty("receiver_id")
    private final String receiverId;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("description")
    private String description;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonCreator
    public Message(@JsonProperty("message_id") String messageId, @JsonProperty("sender_id") String senderId,
            @JsonProperty("receiver_id") String receiverId) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public Message(String messageId, String senderId, String receiverId, String subject, String description,
            LocalDateTime timestamp) {
        this.messageId = (messageId == null || messageId.isBlank()) ? UUID.randomUUID().toString() : messageId;
        this.senderId = (senderId == null || senderId.isBlank()) ? UUID.randomUUID().toString() : senderId;
        this.receiverId = (receiverId == null || receiverId.isBlank()) ? UUID.randomUUID().toString() : receiverId;
        this.subject = subject;
        this.description = description;
        this.timestamp = timestamp;
    }

    /*
     * This method determines how the objects will be compared.
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Message message = (Message) obj;
        return messageId.equals(message.messageId);
    }

    public String getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Message{" + "messageId=" + messageId + ", senderId=" + senderId + ", receiverId=" + receiverId
                + ", message='" + description + '\'' + ", timestamp=" + timestamp + '}';
    }
}
