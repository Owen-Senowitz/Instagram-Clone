package com.example.instagramclonebackend.service;

import com.example.instagramclonebackend.model.dto.User;
import com.example.instagramclonebackend.model.request.UpdateUserRequest;
import com.example.instagramclonebackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser() {
        User user = new User();
        user.setPassword("plaintextPassword");

        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.save(user);

        assertEquals("encodedPassword", savedUser.getPassword());
        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode("plaintextPassword");
    }

    @Test
    void testFindById() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals(1L, foundUser.get().getId());
    }

    @Test
    void testFindByUsername() {
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByUsername("testuser");

        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    void testFindByEmail() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByEmail("test@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.loadUserByUsername("test@example.com"));
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonexistent@example.com"));
    }

    @Test
    void testCheckIfValidOldPassword_Valid() {
        User user = new User();
        user.setPassword("encodedPassword");

        when(passwordEncoder.matches("oldPassword", "encodedPassword")).thenReturn(true);

        boolean isValid = userService.checkIfValidOldPassword(user, "oldPassword");

        assertTrue(isValid);
    }

    @Test
    void testCheckIfValidOldPassword_Invalid() {
        User user = new User();
        user.setPassword("encodedPassword");

        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        boolean isValid = userService.checkIfValidOldPassword(user, "wrongPassword");

        assertFalse(isValid);
    }

    @Test
    void testChangeUserPassword() {
        User user = new User();
        user.setPassword("oldPassword");

        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.changeUserPassword(user, "newPassword");

        assertEquals("encodedNewPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setEmail("test@example.com");

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUsername("newUsername");
        updateUserRequest.setFirstName("NewFirstName");
        updateUserRequest.setLastName("NewLastName");
        updateUserRequest.setBio("New bio");
        updateUserRequest.setProfileImageId(1L);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updateUser("test@example.com", updateUserRequest);

        assertEquals("newUsername", user.getUsername());
        assertEquals("NewFirstName", user.getFirstName());
        assertEquals("NewLastName", user.getLastName());
        assertEquals("New bio", user.getBio());
        assertEquals(1L, user.getProfileImageId());
        verify(userRepository, times(1)).save(user);
    }
}
