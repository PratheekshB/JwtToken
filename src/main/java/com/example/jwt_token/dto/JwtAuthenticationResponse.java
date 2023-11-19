package com.example.jwt_token.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationResponse {

    private String accessToken;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "JwtAuthenticationResponse{" +
                "accessToken='" + accessToken + '\'' +
                '}';
    }
}
