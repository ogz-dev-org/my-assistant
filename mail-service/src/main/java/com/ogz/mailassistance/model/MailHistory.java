package com.ogz.mailassistance.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mail-history-db")
public class MailHistory {
    @org.springframework.data.annotation.Id
    private String Id;
    private String userId;
    private String mailHistoryId;

    public MailHistory(String id, String userId, String mailHistoryId) {
        Id = id;
        this.userId = userId;
        this.mailHistoryId = mailHistoryId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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
