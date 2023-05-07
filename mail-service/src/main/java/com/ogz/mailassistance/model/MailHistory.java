package com.ogz.mailassistance.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mail-history-collection")
public class MailHistory {
    @Id
    private String id;
    private String userId;
    private String mailHistoryId;

    public MailHistory() {
    }

    public MailHistory(String id, String userId, String mailHistoryId) {
        this.id = id;
        this.userId = userId;
        this.mailHistoryId = mailHistoryId;
    }

    public MailHistory(String userId, String mailHistoryId) {
        this.userId = userId;
        this.mailHistoryId = mailHistoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMailHistoryId() {
        return mailHistoryId;
    }

    public void setMailHistoryId(String mailHistoryId) {
        this.mailHistoryId = mailHistoryId;
    }
}
