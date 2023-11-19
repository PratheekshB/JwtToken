package com.example.jwt_token.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutRequest {

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public LogoutRequest(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "LogoutRequest{" +
                "accessToken='" + accessToken + '\'' +
                '}';
    }
}
