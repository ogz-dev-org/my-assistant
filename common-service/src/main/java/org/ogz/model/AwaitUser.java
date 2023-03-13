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
