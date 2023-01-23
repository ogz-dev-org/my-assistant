package org.ogz.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;

@Document(collection = "User-Col")
public class User {
    @Id
    private String id;
    private String googleID;
    private String name;
    private String surname;
    private LocalDateTime registerDate;
    private boolean isActive;
    private HashMap<String,String>emails;
    private HashMap<String,String>accessToken;
    private HashMap<String,String>refreshToken;
    public User() {
    }
    public User(String googleID, String name, String surname, LocalDateTime registerDate, boolean isActive, HashMap<String, String> emails, HashMap<String, String> accessToken, HashMap<String, String> refreshToken) {
        this.googleID = googleID;
        this.name = name;
        this.surname = surname;
        this.registerDate = registerDate;
        this.isActive = isActive;
        this.emails = emails;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoogleID() {
        return googleID;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public HashMap<String, String> getEmails() {
        return emails;
    }

    public void setEmails(HashMap<String, String> emails) {
        this.emails = emails;
    }

    public HashMap<String, String> getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(HashMap<String, String> accessToken) {
        this.accessToken = accessToken;
    }

    public HashMap<String, String> getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(HashMap<String, String> refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", googleID='" + googleID + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", registerDate=" + registerDate +
                ", isActive=" + isActive +
                ", emails=" + emails +
                ", accessToken=" + accessToken +
                ", refreshToken=" + refreshToken +
                '}';
    }
}
