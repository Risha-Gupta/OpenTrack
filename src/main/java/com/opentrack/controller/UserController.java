package com.opentrack.controller;

import com.opentrack.model.User;
import com.opentrack.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getByUsername(userDetails.getUsername());
        return ResponseEntity.ok(Map.of(
            "id", user.getId(), "username", user.getUsername(), "email", user.getEmail(),
            "githubUsername", user.getGithubUsername() != null ? user.getGithubUsername() : "",
            "role", user.getRole()
        ));
    }

    @PutMapping("/me/github")
    public ResponseEntity<?> linkGithub(@AuthenticationPrincipal UserDetails userDetails,
                                         @RequestBody Map<String, String> body) {
        User user = userService.getByUsername(userDetails.getUsername());
        userService.linkGithubAccount(user.getId(), body.get("githubUsername"));
        return ResponseEntity.ok(Map.of("message", "GitHub account linked: " + body.get("githubUsername")));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() { return ResponseEntity.ok(userService.getAllUsers()); }
}