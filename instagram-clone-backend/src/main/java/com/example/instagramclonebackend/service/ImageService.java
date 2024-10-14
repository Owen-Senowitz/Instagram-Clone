package com.example.instagramclonebackend.service;

import com.example.instagramclonebackend.model.dto.Image;
import com.example.instagramclonebackend.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image uploadImage(byte[] fileData) {
        Image image = new Image();
        image.setData(fileData);
        return imageRepository.save(image);
    }

    public Optional<Image> getImageById(Long id) {
        return imageRepository.findById(id);
    }
}
