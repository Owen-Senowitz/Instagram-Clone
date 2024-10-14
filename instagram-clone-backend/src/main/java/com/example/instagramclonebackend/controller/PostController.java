package com.example.instagramclonebackend.controller;

import com.example.instagramclonebackend.model.dto.Image;
import com.example.instagramclonebackend.model.dto.Post;
import com.example.instagramclonebackend.model.dto.User;
import com.example.instagramclonebackend.service.ImageService;
import com.example.instagramclonebackend.service.PostService;
import com.example.instagramclonebackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final ImageService imageService;

    public PostController(PostService postService, UserService userService, ImageService imageService) {
        this.postService = postService;
        this.userService = userService;
        this.imageService = imageService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPost(
            @RequestParam("userId") Long userId,
            @RequestParam("caption") String caption,
            @RequestParam("file") MultipartFile file) {
        try {
            // Find the user
            Optional<User> userOptional = userService.findById(userId);
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            // Save the image
            byte[] imageData = file.getBytes();
            Image image = imageService.uploadImage(imageData);

            // Create the post
            Post post = postService.createPost(userOptional.get(), image, caption);
            return new ResponseEntity<>("Post created successfully with ID: " + post.getId(), HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return postService.getPostById(id)
                .map(post -> new ResponseEntity<>(post, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable Long userId) {
        List<Post> posts = postService.getPostsByUser(userId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}
