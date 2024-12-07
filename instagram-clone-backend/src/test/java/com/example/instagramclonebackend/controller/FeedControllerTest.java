package com.example.instagramclonebackend.controller;

import com.example.instagramclonebackend.model.dto.Post;
import com.example.instagramclonebackend.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FeedControllerTest {
    private PostService postService;
    private FeedController feedController;

    @BeforeEach
    public void setUp() {
        postService = mock(PostService.class);
        feedController = new FeedController(postService);
    }

    @Test
    public void testGetFeed() {
        Post post1 = new Post();
        post1.setId(1L);
        post1.setCaption("First post");

        Post post2 = new Post();
        post2.setId(2L);
        post2.setCaption("Second post");

        List<Post> mockPosts = Arrays.asList(post1, post2);
        when(postService.getAllPostsSortedByDate()).thenReturn(mockPosts);

        ResponseEntity<List<Post>> response = feedController.getFeed();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPosts, response.getBody());
    }

}
