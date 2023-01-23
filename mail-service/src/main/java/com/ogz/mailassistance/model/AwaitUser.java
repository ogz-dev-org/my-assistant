package com.ogz.mailassistance.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "emailFetchAwaitUserDB")
public class AwaitUser {
    @Id
    private String id;
    private String userId;
    private LocalDateTime createDate;
    private LocalDateTime finishedDate;
}
