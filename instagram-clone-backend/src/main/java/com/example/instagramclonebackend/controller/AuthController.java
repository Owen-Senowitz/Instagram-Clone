package com.example.instagramclonebackend.controller;

import com.example.instagramclonebackend.model.UpdatePasswordRequest;
import com.example.instagramclonebackend.model.User;
import com.example.instagramclonebackend.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Value("${secret.key}")
    private String SECRET_KEY;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        Optional<User> existingUserByUsername = userService.findByUsername(user.getUsername());
        if (existingUserByUsername.isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        Optional<User> existingUserByEmail = userService.findByEmail(user.getEmail());
        if (existingUserByEmail.isPresent()) {
            return ResponseEntity.badRequest().body("Email is already registered");
        }

        userService.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();

        return ResponseEntity.ok(token);
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!userService.checkIfValidOldPassword(user, updatePasswordRequest.getOldPassword())) {
            return ResponseEntity.badRequest().body("Invalid old password");
        }

        userService.changeUserPassword(user, updatePasswordRequest.getNewPassword());
        return ResponseEntity.ok("Password updated successfully");
    }
}
