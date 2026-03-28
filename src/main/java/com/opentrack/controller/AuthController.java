package com.opentrack.controller;

import com.opentrack.model.User;
import com.opentrack.service.UserService;
import com.opentrack.util.JwtUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        User user = userService.registerUser(req.getUsername(), req.getEmail(), req.getPassword());
        return ResponseEntity.ok(Map.of(
            "message", "User registered successfully",
            "userId", user.getId(),
            "username", user.getUsername()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
            String token = jwtUtil.generateToken(req.getUsername());
            return ResponseEntity.ok(Map.of("token", token, "tokenType", "Bearer", "username", req.getUsername()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }

    @Data static class RegisterRequest {
        @NotBlank @Size(min = 3, max = 50) private String username;
        @Email @NotBlank private String email;
        @NotBlank @Size(min = 8) private String password;
    }

    @Data static class LoginRequest {
        @NotBlank private String username;
        @NotBlank private String passwo
    }
}