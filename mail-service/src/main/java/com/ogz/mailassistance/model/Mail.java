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
    private String content;
    private String fromUser;
    private String toUser;
    private LocalDateTime sendingDate;

    public Mail(String content, String fromUser, String toUser, LocalDateTime sendingDate) {
        this.content = content;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.sendingDate = sendingDate;
    }
}
