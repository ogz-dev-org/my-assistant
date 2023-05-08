package org.ogz.dto;

import java.util.List;

public class ReminderEventDto {
    private String from;
    private List<String> to;
    private String title;
    private String content;

    private String reminderId;

    public String getReminderId() {
        return reminderId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ReminderEventDto(String from, List<String> to, String title, String content,String reminderId) {
        this.from = from;
        this.to = to;
        this.title = title;
        this.content = content;
        this.reminderId = reminderId;
    }
}
