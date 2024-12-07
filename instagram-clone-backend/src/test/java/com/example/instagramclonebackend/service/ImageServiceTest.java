package com.example.instagramclonebackend.service;

import com.example.instagramclonebackend.model.dto.Image;
import com.example.instagramclonebackend.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ImageServiceTest {

    private ImageRepository imageRepository;
    private ImageService imageService;

    @BeforeEach
    public void setUp() {
        imageRepository = mock(ImageRepository.class);
        imageService = new ImageService(imageRepository);
    }

    @Test
    public void testUploadImage() {
        byte[] fileData = "test image data".getBytes();
        Image mockImage = new Image();
        mockImage.setId(1L);
        mockImage.setData(fileData);

        when(imageRepository.save(any(Image.class))).thenReturn(mockImage);

        Image uploadedImage = imageService.uploadImage(fileData);

        assertEquals(mockImage, uploadedImage);

        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    public void testGetImageByIdFound() {
        Image mockImage = new Image();
        mockImage.setId(1L);
        mockImage.setData("test image data".getBytes());

        when(imageRepository.findById(1L)).thenReturn(Optional.of(mockImage));

        Optional<Image> image = imageService.getImageById(1L);

        assertTrue(image.isPresent());
        assertEquals(mockImage, image.get());
        verify(imageRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetImageByIdNotFound() {
        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Image> image = imageService.getImageById(1L);

        assertTrue(image.isEmpty());
        verify(imageRepository, times(1)).findById(1L);
    }
}

