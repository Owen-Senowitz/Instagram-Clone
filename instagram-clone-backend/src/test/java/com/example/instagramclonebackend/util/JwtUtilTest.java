package com.example.instagramclonebackend.util;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtil = new JwtUtil("test-secret-key");
    }

    @Test
    public void testGenerateToken() {
        when(userDetails.getUsername()).thenReturn("testuser");

        String token = jwtUtil.generateToken(userDetails);

        assertNotNull(token);
        assertEquals("testuser", jwtUtil.extractUsername(token));
    }

    @Test
    public void testExtractUsername() {
        when(userDetails.getUsername()).thenReturn("testuser");

        String token = jwtUtil.generateToken(userDetails);
        String username = jwtUtil.extractUsername(token);

        assertEquals("testuser", username);
    }

    @Test
    public void testExtractExpiration() {
        when(userDetails.getUsername()).thenReturn("testuser");

        String token = jwtUtil.generateToken(userDetails);
        Date expiration = jwtUtil.extractExpiration(token);

        assertNotNull(expiration);
        assertTrue(expiration.after(new Date())); // Token should expire in the future
    }

    @Test
    public void testValidateTokenValid() {
        when(userDetails.getUsername()).thenReturn("testuser");

        String token = jwtUtil.generateToken(userDetails);
        boolean isValid = jwtUtil.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    public void testValidateTokenExpired() {
        when(userDetails.getUsername()).thenReturn("testuser");

        String token = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 10)) // 10 hours ago
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 60)) // 1 hour ago
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, "test-secret-key")
                .compact();

        boolean isValid = jwtUtil.validateToken(token, userDetails);

        assertFalse(isValid);
    }


    @Test
    public void testValidateTokenWithDifferentUsername() {
        when(userDetails.getUsername()).thenReturn("differentuser");

        String token = jwtUtil.generateToken(userDetails);

        // Simulate token with a different username
        String invalidToken = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, "test-secret-key")
                .compact();

        boolean isValid = jwtUtil.validateToken(invalidToken, userDetails);

        assertFalse(isValid);
    }
}
