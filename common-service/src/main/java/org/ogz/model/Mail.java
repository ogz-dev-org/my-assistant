package org.ogz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "email-collection")
public class Mail {
    @Id
    private String id;
    private String subject;
    private String content;
    private String fromUser;
    private String toUser;
    private Date sendingDate;
    private String originalMailId;
    private List<String> labels;
    public Mail(String content, String subject,String fromUser, String toUser, Date sendingDate,String originalMailId
            ,List<String> labels) {
        this.content = content;
        this.subject = subject;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.sendingDate = sendingDate;
        this.originalMailId = originalMailId;
        this.labels = labels;
    }

    public Mail(Mail mail) {
        this.id = mail.id;
        this.subject = mail.subject;
        this.content = mail.content;
        this.fromUser = mail.fromUser;
        this.toUser = mail.toUser;
        this.sendingDate = mail.sendingDate;
        this.originalMailId = mail.originalMailId;
        this.labels = mail.labels;
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
