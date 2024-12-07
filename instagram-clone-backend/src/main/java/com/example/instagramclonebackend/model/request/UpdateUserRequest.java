package com.example.instagramclonebackend.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    @JsonProperty(value = "username")
    private String username;

    @JsonProperty(value = "firstName")
    private String firstName;

    @JsonProperty(value = "lastName")
    private String lastName;

    @JsonProperty(value = "bio")
    private String bio;

    @JsonProperty(value = "profileImageId")
    private Long profileImageId;
}
