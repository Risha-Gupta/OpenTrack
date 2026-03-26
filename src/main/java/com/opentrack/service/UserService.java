package com.opentrack.service;

import com.opentrack.exception.ResourceNotFoundException;
import com.opentrack.model.User;
import com.opentrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRole().name())
            .disabled(!user.isActive())
            .build();
    }

    @Transactional
    public User registerUser(String username, String email, String password) {
        if (userRepository.existsByUsername(username))
            throw new IllegalArgumentException("Username already exists: " + username);
        if (userRepository.existsByEmail(email))
            throw new IllegalArgumentException("Email already registered: " + email);
        User user = User.builder()
            .username(username).email(email)
            .password(passwordEncoder.encode(password))
            .role(User.Role.USER).build();
        return userRepository.save(user);
    }

    public User getById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    @Transactional
    public User linkGithubAccount(Long userId, String githubUsername) {
        User user = getById(userId);
        user.setGithubUsername(githubUsername);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() { return userRepository.findAll(); }
}