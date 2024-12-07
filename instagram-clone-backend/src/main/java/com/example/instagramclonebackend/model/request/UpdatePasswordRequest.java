package com.example.instagramclonebackend.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequest {

    @JsonProperty(value = "oldPassword")
    private String oldPassword;

    @JsonProperty(value = "newPassword")
    private String newPassword;
}
