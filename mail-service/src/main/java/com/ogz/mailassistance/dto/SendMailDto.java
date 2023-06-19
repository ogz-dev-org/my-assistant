package com.ogz.mailassistance.dto;

import java.util.List;

public class SendMailDto {
    private String content;
    private String title;
    private String toUserList;

    public SendMailDto(String content, String title, String toUserList) {
        this.content = content;
        this.title = title;
        this.toUserList = toUserList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getToUserList() {
        return toUserList;
    }

    public void setToUserList(String toUserList) {
        this.toUserList = toUserList;
    }

    @Override
    public String toString() {
        return "SendMailDto{" +
                "content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", toUserList='" + toUserList + '\'' +
                '}';
    }
}
