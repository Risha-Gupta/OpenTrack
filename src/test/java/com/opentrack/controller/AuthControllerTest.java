package com.opentrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentrack.model.User;
import com.opentrack.service.UserService;
import com.opentrack.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Map;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private UserService userService;
    @MockBean private JwtUtil jwtUtil;
    @MockBean private AuthenticationManager authenticationManager;

    @Test
    void register_validRequest_returns200() throws Exception {
        User mockUser = User.builder().id(1L).username("testuser").email("test@example.com").build();
        when(userService.registerUser(any(), any(), any())).thenReturn(mockUser);
        Map<String, String> req = Map.of("username", "testuser", "email", "test@example.com", "password", "password123");
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void login_validCredentials_returnsToken() throws Exception {
        when(authenticationManager.authenticate(any()))
            .thenReturn(new UsernamePasswordAuthenticationToken("testuser", "pass"));
        when(jwtUtil.generateToken("testuser")).thenReturn("mock-jwt-token");
        Map<String, String> req = Map.of("username", "testuser", "password", "password123");
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }
}