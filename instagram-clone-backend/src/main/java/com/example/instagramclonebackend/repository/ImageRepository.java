package com.example.instagramclonebackend.repository;

import com.example.instagramclonebackend.model.dto.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
