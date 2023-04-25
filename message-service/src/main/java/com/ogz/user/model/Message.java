package com.ogz.user.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "message-db")
public class Message {
    @Id
    private String id;
    private String fromUser;
    private String message;
    private String toUser;
    private String toGroup;
    private LocalDateTime sendDate;

    public Message(String fromUser, String message, String toUser, String toGroup, LocalDateTime sendDate) {
        this.fromUser = fromUser;
        this.message = message;
        this.toUser = toUser;
        this.toGroup = toGroup;
        this.sendDate = sendDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getToGroup() {
        return toGroup;
    }

    public void setToGroup(String toGroup) {
        this.toGroup = toGroup;
    }

    public LocalDateTime getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }
}
