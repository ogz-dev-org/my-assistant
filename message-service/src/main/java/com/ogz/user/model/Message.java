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

}
