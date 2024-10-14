package com.example.instagramclonebackend.repository;

import com.example.instagramclonebackend.model.dto.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
}
