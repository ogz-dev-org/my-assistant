package com.ogz.mailassistance.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "email-collection")
public class Message {
    @Id
    private String id;
    private String content;
    private String fromUser;
    private String toUser;
    private String sendingDate;

    public Message(String content, String fromUser, String toUser, String sendingDate) {
        this.content = content;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.sendingDate = sendingDate;
    }
}
