package org.ogz.model;

import org.ogz.dto.AccountPairs;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;

@Document(collection = "user")
public class User {
    @Id
    private String id;
    private String googleID;
    private String name;
    private String surname;
    private String gmail;
    private String avatarPicture;
    private LocalDateTime registerDate;
    private boolean isActive;
    private AccountPairs accessToken;
    private AccountPairs refreshToken;
    public User() {
    }
    public User(String googleID, String name, String surname, String avatarPicture,
                LocalDateTime registerDate, boolean isActive, AccountPairs accessToken,
                AccountPairs refreshToken, String gmail) {
        this.googleID = googleID;
        this.name = name;
        this.surname = surname;
        this.gmail = gmail;
        this.avatarPicture = avatarPicture;
        this.registerDate = registerDate;
        this.isActive = isActive;
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

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getAvatarPicture() {
        return avatarPicture;
    }

    public void setAvatarPicture(String avatarPicture) {
        this.avatarPicture = avatarPicture;
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

    public AccountPairs getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccountPairs accessToken) {
        this.accessToken = accessToken;
    }

    public AccountPairs getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(AccountPairs refreshToken) {
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
                ", accessToken=" + accessToken +
                ", refreshToken=" + refreshToken +
                '}';
    }
}
