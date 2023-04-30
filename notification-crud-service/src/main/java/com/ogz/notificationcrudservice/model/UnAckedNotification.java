package com.ogz.notificationcrudservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "un-acked-notification-collection")
public class UnAckedNotification {
    @Id
    private String id;
    private String eventId;
    private String ownerId;
    private EventType eventType;

    public UnAckedNotification(String eventId, String ownerId,EventType eventType) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.ownerId = ownerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
