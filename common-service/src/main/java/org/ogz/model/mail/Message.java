package org.ogz.model.mail;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "message-db")

public class Message{
    @Id
    private String id;
    private String ownerId;
    private String Date;
    private String To;
    private String From;
    private String Subject;
    private Body body;

    public Message() {
    }

    public Message(String id, String ownerId, String date, String to, String from, String subject, Body body) {
        this.id = id;
        this.ownerId = ownerId;
        Date = date;
        To = to;
        From = from;
        Subject = subject;
        this.body = body;
    }

    public Message(String ownerId, String date, String to, String from, String subject, Body body) {
        this.ownerId = ownerId;
        Date = date;
        To = to;
        From = from;
        Subject = subject;
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
