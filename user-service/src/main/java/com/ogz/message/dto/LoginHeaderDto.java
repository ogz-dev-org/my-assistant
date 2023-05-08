package com.ogz.message.dto;

public class LoginHeaderDto {

    private String idToken;
    private String authToken;

    public LoginHeaderDto(String idToken, String authToken) {
        this.idToken = idToken;
        this.authToken = authToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
