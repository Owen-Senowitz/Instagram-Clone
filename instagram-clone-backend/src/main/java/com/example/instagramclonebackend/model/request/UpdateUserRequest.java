package com.example.instagramclonebackend.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UpdateUserRequest {

    @JsonProperty(value = "username")
    private String username;

    @JsonProperty(value = "firstName")
    private String firstName;

    @JsonProperty(value = "lastName")
    private String lastName;

    @JsonProperty(value = "bio")
    private String bio;

    @JsonProperty(value = "profilePictureUrl")
    private String profilePictureUrl;
}
