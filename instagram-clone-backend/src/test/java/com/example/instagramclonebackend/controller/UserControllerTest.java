package com.example.instagramclonebackend.controller;

import com.example.instagramclonebackend.model.request.LoginRequest;
import com.example.instagramclonebackend.model.request.SignUpRequest;
import com.example.instagramclonebackend.model.request.UpdatePasswordRequest;
import com.example.instagramclonebackend.model.dto.User;
import com.example.instagramclonebackend.model.request.UpdateUserRequest;
import com.example.instagramclonebackend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private UserController userController;

    @BeforeEach
    public void setUp() {
        authenticationManager = mock(AuthenticationManager.class);
        userService = mock(UserService.class);
        userController = new UserController(authenticationManager, userService, "test-secret");
    }

    @Test
    public void testSignUpSuccess() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("newuser");
        signUpRequest.setPassword("password123");
        signUpRequest.setEmail("newuser@example.com");

        when(userService.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userService.findByEmail("newuser@example.com")).thenReturn(Optional.empty());

        ResponseEntity<String> response = userController.signUp(signUpRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully", response.getBody());
        verify(userService, times(1)).save(any(User.class));
    }

    @Test
    public void testSignUpUsernameTaken() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("existinguser");
        signUpRequest.setPassword("password123");
        signUpRequest.setEmail("newuser@example.com");

        when(userService.findByUsername("existinguser")).thenReturn(Optional.of(new User()));

        ResponseEntity<String> response = userController.signUp(signUpRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Username is already taken", response.getBody());
        verify(userService, never()).save(any(User.class));
    }

    @Test
    public void testSignUpEmailAlreadyExists() {
        // Prepare a SignUpRequest object
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("newuser");
        signUpRequest.setPassword("password123");
        signUpRequest.setEmail("existinguser@example.com");

        // Mock the behavior of userService to simulate an existing email
        when(userService.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userService.findByEmail("existinguser@example.com")).thenReturn(Optional.of(new User()));

        // Call the signup method in the controller
        ResponseEntity<String> response = userController.signUp(signUpRequest);

        // Verify the response
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Email is already registered", response.getBody());

        // Verify that save was not called because the email already exists
        verify(userService, never()).save(any(User.class));
    }

    @Test
    public void testLoginSuccess() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@example.com");
        loginRequest.setPassword("password123");

        UserDetails userDetails = mock(UserDetails.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        ResponseEntity<String> response = userController.login(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testUpdatePasswordSuccess() {
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        updatePasswordRequest.setOldPassword("oldpassword");
        updatePasswordRequest.setNewPassword("newpassword");

        User user = new User();
        user.setEmail("user@example.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn("user@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userService.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userService.checkIfValidOldPassword(user, "oldpassword")).thenReturn(true);

        ResponseEntity<String> response = userController.updatePassword(updatePasswordRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Password updated successfully", response.getBody());
        verify(userService, times(1)).changeUserPassword(user, "newpassword");
    }

    @Test
    public void testUpdatePasswordInvalidOldPassword() {
        // Prepare UpdatePasswordRequest with invalid old password
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        updatePasswordRequest.setOldPassword("wrongpassword");
        updatePasswordRequest.setNewPassword("newpassword");

        // Mock the authenticated user and security context
        User user = new User();
        user.setEmail("user@example.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn("user@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock the service behavior
        when(userService.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userService.checkIfValidOldPassword(user, "wrongpassword")).thenReturn(false);

        // Call the updatePassword method
        ResponseEntity<String> response = userController.updatePassword(updatePasswordRequest);

        // Verify the response
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Invalid old password", response.getBody());

        // Verify that the password change method is not called
        verify(userService, never()).changeUserPassword(any(User.class), anyString());
    }


    @Test
    public void testGetUserProfileSuccess() {
        User user = new User();
        user.setEmail("user@example.com");

        UserDetails userDetails = mock(UserDetails.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userService.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        User responseUser = userController.getUserProfile();

        assertEquals(user, responseUser);
        verify(userService, times(1)).findByEmail("user@example.com");
    }

    @Test
    public void testUpdateUserSuccess() {
        // Prepare UpdateUserRequest object
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setFirstName("John");
        updateUserRequest.setLastName("Doe");

        // Mock the authenticated user and security context
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn("user@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Call the updateUser method
        ResponseEntity<String> response = userController.updateUser(updateUserRequest);

        // Verify the response
        assertEquals(200, response.getStatusCode().value());
        assertEquals("User information updated successfully", response.getBody());

        // Verify the service method was called with the correct arguments
        verify(userService, times(1)).updateUser("user@example.com", updateUserRequest);
    }
}
