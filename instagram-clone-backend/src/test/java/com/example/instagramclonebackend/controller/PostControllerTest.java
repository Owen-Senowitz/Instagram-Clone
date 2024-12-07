package com.example.instagramclonebackend.controller;

import com.example.instagramclonebackend.model.dto.Image;
import com.example.instagramclonebackend.model.dto.Post;
import com.example.instagramclonebackend.model.dto.User;
import com.example.instagramclonebackend.service.ImageService;
import com.example.instagramclonebackend.service.PostService;
import com.example.instagramclonebackend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PostControllerTest {

    private PostService postService;
    private UserService userService;
    private ImageService imageService;
    private PostController postController;

    @BeforeEach
    public void setUp() {
        postService = mock(PostService.class);
        userService = mock(UserService.class);
        imageService = mock(ImageService.class);
        postController = new PostController(postService, userService, imageService);
    }

    @Test
    public void testCreatePostSuccess() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", "image data".getBytes());
        User user = new User();
        user.setId(1L);
        Image image = new Image();
        image.setId(1L);
        Post post = new Post();
        post.setId(1L);

        when(userService.findById(1L)).thenReturn(Optional.of(user));
        when(imageService.uploadImage(mockFile.getBytes())).thenReturn(image);
        when(postService.createPost(user, image, "Test Caption")).thenReturn(post);

        ResponseEntity<String> response = postController.createPost(1L, "Test Caption", mockFile);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Post created successfully with ID: 1", response.getBody());
        verify(userService, times(1)).findById(1L);
        verify(imageService, times(1)).uploadImage(mockFile.getBytes());
        verify(postService, times(1)).createPost(user, image, "Test Caption");
    }

    @Test
    public void testCreatePostUserNotFound() {
        MockMultipartFile mockFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", "image data".getBytes());

        when(userService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = postController.createPost(1L, "Test Caption", mockFile);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
        verify(userService, times(1)).findById(1L);
        verifyNoInteractions(imageService);
        verifyNoInteractions(postService);
    }

    @Test
    public void testCreatePostImageUploadFailureOnFileRead() throws Exception {
        User user = new User();
        user.setId(1L);
        when(userService.findById(1L)).thenReturn(Optional.of(user));

        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getBytes()).thenThrow(new IOException("Failed to read file"));

        ResponseEntity<String> response = postController.createPost(1L, "Test Caption", mockFile);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Failed to upload image", response.getBody());

        verifyNoInteractions(imageService);
        verifyNoInteractions(postService);
    }



    @Test
    public void testGetPostByIdSuccess() {
        Post post = new Post();
        post.setId(1L);

        when(postService.getPostById(1L)).thenReturn(Optional.of(post));

        ResponseEntity<Post> response = postController.getPostById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(post, response.getBody());
        verify(postService, times(1)).getPostById(1L);
    }

    @Test
    public void testGetPostByIdNotFound() {
        when(postService.getPostById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Post> response = postController.getPostById(1L);

        assertEquals(404, response.getStatusCodeValue());
        verify(postService, times(1)).getPostById(1L);
    }

    @Test
    public void testGetPostsByUser() {
        Post post1 = new Post();
        post1.setId(1L);
        Post post2 = new Post();
        post2.setId(2L);
        List<Post> posts = Arrays.asList(post1, post2);

        when(postService.getPostsByUser(1L)).thenReturn(posts);

        ResponseEntity<List<Post>> response = postController.getPostsByUser(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(posts, response.getBody());
        verify(postService, times(1)).getPostsByUser(1L);
    }

    @Test
    public void testGetAllPosts() {
        Post post1 = new Post();
        post1.setId(1L);
        Post post2 = new Post();
        post2.setId(2L);
        List<Post> posts = Arrays.asList(post1, post2);

        when(postService.getAllPosts()).thenReturn(posts);

        ResponseEntity<List<Post>> response = postController.getAllPosts();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(posts, response.getBody());
        verify(postService, times(1)).getAllPosts();
    }
}
