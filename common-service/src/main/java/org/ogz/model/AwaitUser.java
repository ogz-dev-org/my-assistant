package org.ogz.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "await-user")
public class AwaitUser {
    @Id
    private String id;
    private String userId;
    private int mailCount;
    private List<String> mailIds;
    private LocalDateTime createDate;
    private LocalDateTime finishedDate;

    public AwaitUser() {
    }

    public AwaitUser(String userId, int mailCount, List<String> mailIds) {
        this.userId = userId;
        this.createDate = LocalDateTime.now();
        this.mailCount = mailCount;
        this.mailIds = mailIds;
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

    public int getMailCount() {
        return mailCount;
    }

    public void setMailCount(int mailCount) {
        this.mailCount = mailCount;
    }

    public List<String> getMailIds() {
        return mailIds;
    }

    public void setMailIds(List<String> mailIds) {
        this.mailIds = mailIds;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(LocalDateTime finishedDate) {
        this.finishedDate = finishedDate;
    }

    @Override
    public String toString() {
        return "AwaitUser{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", mailCount=" + mailCount +
                ", mailIds=" + mailIds +
                ", createDate=" + createDate +
                ", finishedDate=" + finishedDate +
                '}';
    }
}
