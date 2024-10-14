package com.example.instagramclonebackend.controller;

import com.example.instagramclonebackend.model.dto.Post;
import com.example.instagramclonebackend.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/feed")
public class FeedController {

    private final PostService postService;

    public FeedController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getFeed() {
        List<Post> posts = postService.getAllPostsSortedByDate();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}
