package com.example.instagramclonebackend.service;

import com.example.instagramclonebackend.model.dto.Image;
import com.example.instagramclonebackend.model.dto.Post;
import com.example.instagramclonebackend.model.dto.User;
import com.example.instagramclonebackend.repository.PostRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(User user, Image image, String caption) {
        Post post = new Post();
        post.setUser(user);
        post.setImage(image);
        post.setCaption(caption);
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> getPostsByUser(Long userId) {
        return postRepository.findByUserId(userId);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getAllPostsSortedByDate() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
