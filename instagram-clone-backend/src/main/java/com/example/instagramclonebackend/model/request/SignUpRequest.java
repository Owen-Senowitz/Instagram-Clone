package com.example.instagramclonebackend.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SignUpRequest {

    @JsonProperty(value = "username")
    private String username;

    @JsonProperty(value = "password")
    private String password;

    @JsonProperty(value = "email")
    private String email;
}
