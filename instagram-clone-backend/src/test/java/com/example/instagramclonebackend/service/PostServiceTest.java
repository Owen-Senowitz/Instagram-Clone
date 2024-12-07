package com.example.instagramclonebackend.service;

import com.example.instagramclonebackend.model.dto.Image;
import com.example.instagramclonebackend.model.dto.Post;
import com.example.instagramclonebackend.model.dto.User;
import com.example.instagramclonebackend.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    private PostRepository postRepository;
    private PostService postService;

    @BeforeEach
    public void setUp() {
        postRepository = mock(PostRepository.class);
        postService = new PostService(postRepository);
    }

    @Test
    public void testCreatePost() {
        User user = new User();
        user.setId(1L);
        Image image = new Image();
        image.setId(1L);
        String caption = "Test Caption";

        Post mockPost = new Post();
        mockPost.setId(1L);
        mockPost.setUser(user);
        mockPost.setImage(image);
        mockPost.setCaption(caption);
        mockPost.setCreatedAt(LocalDateTime.now());

        when(postRepository.save(any(Post.class))).thenReturn(mockPost);

        Post savedPost = postService.createPost(user, image, caption);

        assertEquals(mockPost, savedPost);

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository, times(1)).save(postCaptor.capture());

        Post capturedPost = postCaptor.getValue();
        assertEquals(user, capturedPost.getUser());
        assertEquals(image, capturedPost.getImage());
        assertEquals(caption, capturedPost.getCaption());
    }

    @Test
    public void testGetPostById() {
        Post mockPost = new Post();
        mockPost.setId(1L);

        when(postRepository.findById(1L)).thenReturn(Optional.of(mockPost));

        Optional<Post> post = postService.getPostById(1L);

        assertEquals(Optional.of(mockPost), post);
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetPostsByUser() {
        Post post1 = new Post();
        post1.setId(1L);
        Post post2 = new Post();
        post2.setId(2L);

        List<Post> posts = Arrays.asList(post1, post2);

        when(postRepository.findByUserId(1L)).thenReturn(posts);

        List<Post> userPosts = postService.getPostsByUser(1L);

        assertEquals(posts, userPosts);
        verify(postRepository, times(1)).findByUserId(1L);
    }

    @Test
    public void testGetAllPosts() {
        Post post1 = new Post();
        post1.setId(1L);
        Post post2 = new Post();
        post2.setId(2L);

        List<Post> posts = Arrays.asList(post1, post2);

        when(postRepository.findAll()).thenReturn(posts);

        List<Post> allPosts = postService.getAllPosts();

        assertEquals(posts, allPosts);
        verify(postRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllPostsSortedByDate() {
        Post post1 = new Post();
        post1.setId(1L);
        post1.setCreatedAt(LocalDateTime.of(2023, 12, 6, 10, 0));
        Post post2 = new Post();
        post2.setId(2L);
        post2.setCreatedAt(LocalDateTime.of(2023, 12, 7, 10, 0));

        List<Post> sortedPosts = Arrays.asList(post2, post1);

        when(postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))).thenReturn(sortedPosts);

        List<Post> result = postService.getAllPostsSortedByDate();

        assertEquals(sortedPosts, result);
        verify(postRepository, times(1)).findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
