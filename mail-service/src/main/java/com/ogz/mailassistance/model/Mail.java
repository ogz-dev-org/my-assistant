package com.ogz.mailassistance.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "email-collection")
public class Mail {
    @Id
    private String id;
    private String subject;
    private String content;
    private String fromUser;
    private String toUser;
    private LocalDateTime sendingDate;
    private String originalMailId;
    public Mail(String content, String subject,String fromUser, String toUser, LocalDateTime sendingDate,String originalMailId) {
        this.content = content;
        this.subject = subject;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.sendingDate = sendingDate;
        this.originalMailId = originalMailId;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", fromUser='" + fromUser + '\'' +
                ", toUser='" + toUser + '\'' +
                ", sendingDate=" + sendingDate +
                ", originalMailId='" + originalMailId + '\'' +
                '}';
    }
}
