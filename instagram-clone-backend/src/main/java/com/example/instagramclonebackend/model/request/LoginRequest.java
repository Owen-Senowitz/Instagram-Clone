package com.example.instagramclonebackend.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class LoginRequest {

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "password")
    private String password;
}
