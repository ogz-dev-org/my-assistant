package com.ogz.message.dto;

public class MessageSendDto {
    private String toUser;
    private String toGroup;
    private String message;

    public MessageSendDto(String toUser, String toGroup, String message) {
        this.toUser = toUser;
        this.toGroup = toGroup;
        this.message = message;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getToGroup() {
        return toGroup;
    }

    public void setToGroup(String toGroup) {
        this.toGroup = toGroup;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
