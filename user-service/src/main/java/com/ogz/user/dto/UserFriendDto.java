package com.ogz.user.dto;

import java.time.LocalDateTime;
import java.util.HashMap;

public class UserFriendDto {
    private String id;
    private String name;
    private String username;
    private HashMap<String,String> emailAddresses;
    private LocalDateTime registerDate;

    public UserFriendDto(String id,String name, String username, HashMap<String, String> emailAddresses,
                          LocalDateTime registerDate) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.emailAddresses = emailAddresses;
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

    public HashMap<String, String> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(HashMap<String, String> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }
}
