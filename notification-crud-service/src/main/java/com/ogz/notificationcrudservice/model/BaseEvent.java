package com.ogz.notificationcrudservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "event-collection")
public class BaseEvent {

    @Id
    private String id;

}
