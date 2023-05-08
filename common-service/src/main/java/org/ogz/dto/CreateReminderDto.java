package org.ogz.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CreateReminderDto {

    private String title;
    private String content;
    //private LocalDateTime triggerDate;
    private List<String> userList;

    public CreateReminderDto(String title, String content, LocalDateTime triggerDate, List<String> userList) {
        this.title = title;
        this.content = content;
//        this.triggerDate = triggerDate;
        this.userList = userList;
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

//    public LocalDateTime getTriggerDate() {
//        return triggerDate;
//    }
//
//    public void setTriggerDate(LocalDateTime triggerDate) {
//        this.triggerDate = triggerDate;
//    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }
}
