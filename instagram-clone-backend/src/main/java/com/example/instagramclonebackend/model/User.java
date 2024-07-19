package com.example.instagramclonebackend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "users")
@Getter
@Setter
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private String email;
}