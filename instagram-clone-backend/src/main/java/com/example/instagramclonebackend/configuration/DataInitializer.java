package com.example.instagramclonebackend.configuration;

import com.example.instagramclonebackend.model.dto.Image;
import com.example.instagramclonebackend.model.dto.User;
import com.example.instagramclonebackend.service.ImageService;
import com.example.instagramclonebackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@Configuration
public class DataInitializer {

    private final UserService userService;
    private final ImageService imageService;

    public DataInitializer(UserService userService, ImageService imageService) {
        this.userService = userService;
        this.imageService = imageService;
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

            uploadTestImage("test1.jpg");

            // Add more users as needed
            log.info("Test users added to the database.");
        };
    }

    private void uploadTestImage(String fileName) {
        try {
            // Load the image file from resources (assuming images are stored in src/main/resources/static/images)
            ClassPathResource imgFile = new ClassPathResource("static/images/" + fileName);
            byte[] imageBytes = Files.readAllBytes(imgFile.getFile().toPath());

            imageService.uploadImage(imageBytes); // Pass the byte array to the service method
        } catch (IOException e) {
            log.error("Failed to upload test image: " + fileName);
        }
    }
}
