package com.example.jwt_token.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRegistrationRequest {

    private String username;
    private String password;
    private String email;
    private List<String> roles;

    public UserRegistrationRequest() {
    }

    public UserRegistrationRequest(String username, String password, String email, List<String> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

}
