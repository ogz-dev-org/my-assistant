package com.ogz.message.dto;

import java.time.LocalDateTime;

public class UserFriendDto {
    private String id;
    private String name;
    private String username;
    private String gmail;
    private LocalDateTime registerDate;

    public UserFriendDto(String id,String name, String username, String gmail, LocalDateTime registerDate) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.gmail = gmail;
        this.registerDate = registerDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }
}
