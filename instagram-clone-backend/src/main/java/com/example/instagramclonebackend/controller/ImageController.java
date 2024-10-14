package com.example.instagramclonebackend.controller;

import com.example.instagramclonebackend.model.dto.Image;
import com.example.instagramclonebackend.service.ImageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Convert MultipartFile to byte[]
            byte[] fileBytes = file.getBytes();

            // Pass byte[] to ImageService
            Image image = imageService.uploadImage(fileBytes);
            return new ResponseEntity<>("Image uploaded successfully with ID: " + image.getId(), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImageById(@PathVariable Long id) {
        return imageService.getImageById(id)
                .map(image -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.set(HttpHeaders.CONTENT_TYPE, "image/jpeg"); // Or other image content type
                    return new ResponseEntity<>(image.getData(), headers, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
