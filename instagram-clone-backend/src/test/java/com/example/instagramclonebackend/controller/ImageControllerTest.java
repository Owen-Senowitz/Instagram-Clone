package com.example.instagramclonebackend.controller;

import com.example.instagramclonebackend.model.dto.Image;
import com.example.instagramclonebackend.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ImageControllerTest {

    private ImageService imageService;
    private ImageController imageController;

    @BeforeEach
    public void setUp() {
        imageService = mock(ImageService.class);
        imageController = new ImageController(imageService);
    }

    @Test
    public void testUploadImageSuccess() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", "image data".getBytes());
        Image image = new Image();
        image.setId(1L);

        when(imageService.uploadImage(mockFile.getBytes())).thenReturn(image);

        ResponseEntity<String> response = imageController.uploadImage(mockFile);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Image uploaded successfully with ID: 1", response.getBody());
        verify(imageService, times(1)).uploadImage(mockFile.getBytes());
    }

    @Test
    public void testUploadImageIOException() throws Exception {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getBytes()).thenThrow(new IOException("Failed to read file"));

        ResponseEntity<String> response = imageController.uploadImage(mockFile);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Failed to upload image", response.getBody());
        verify(mockFile, times(1)).getBytes();
        verifyNoInteractions(imageService);
    }

    @Test
    public void testGetImageByIdSuccess() {
        Image image = new Image();
        image.setId(1L);
        image.setData("image data".getBytes());

        when(imageService.getImageById(1L)).thenReturn(Optional.of(image));

        ResponseEntity<byte[]> response = imageController.getImageById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("image data", new String(response.getBody()));
        assertEquals("image/jpeg", response.getHeaders().getContentType().toString());
        verify(imageService, times(1)).getImageById(1L);
    }

    @Test
    public void testGetImageByIdNotFound() {
        when(imageService.getImageById(1L)).thenReturn(Optional.empty());

        ResponseEntity<byte[]> response = imageController.getImageById(1L);

        assertEquals(404, response.getStatusCodeValue());
        verify(imageService, times(1)).getImageById(1L);
    }
}
