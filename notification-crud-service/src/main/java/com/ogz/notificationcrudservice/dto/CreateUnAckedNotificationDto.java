package com.ogz.notificationcrudservice.dto;

import com.ogz.notificationcrudservice.model.EventType;

public class CreateUnAckedNotificationDto {
    private String eventId;
    private String ownerId;
    private EventType eventType;

    public CreateUnAckedNotificationDto(String eventId, String ownerId, EventType eventType) {
        this.eventId = eventId;
        this.ownerId = ownerId;
        this.eventType = eventType;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
