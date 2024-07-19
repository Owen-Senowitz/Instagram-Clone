package com.example.instagramclonebackend.model;

import lombok.Getter;

@Getter
public class UpdatePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
