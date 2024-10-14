package com.example.instagramclonebackend.configuration;

import com.example.instagramclonebackend.model.dto.User;
import com.example.instagramclonebackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DataInitializer {

    private final UserService userService;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Test user 1
            User user1 = new User();
            user1.setUsername("testuser1");
            user1.setPassword("password123");
            user1.setEmail("testuser1@example.com");
            user1.setFirstName("Test");
            user1.setLastName("User1");
            user1.setBio("This is a bio for testuser1.");
            user1.setProfilePictureUrl("http://example.com/user1.jpg");
            userService.save(user1);

            // Test user 2
            User user2 = new User();
            user2.setUsername("testuser2");
            user2.setPassword("password123");
            user2.setEmail("testuser2@example.com");
            user2.setFirstName("Test");
            user2.setLastName("User2");
            user2.setBio("This is a bio for testuser2.");
            user2.setProfilePictureUrl("http://example.com/user2.jpg");
            userService.save(user2);

            // Add more users as needed
            log.info("Test users added to the database.");
        };
    }
}
