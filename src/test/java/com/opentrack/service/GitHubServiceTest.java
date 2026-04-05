package com.opentrack.service;

import com.opentrack.config.GitHubApiConfig;
import com.opentrack.dto.GitHubEventDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitHubServiceTest {

    @Mock private RestTemplate githubRestTemplate;
    @Mock private GitHubApiConfig gitHubApiConfig;
    @InjectMocks private GitHubService gitHubService;

    @BeforeEach
    void setUp() { when(gitHubApiConfig.getBaseUrl()).thenReturn("https://api.github.com"); }

    @Test
    void getUserEvents_validUser_returnsEvents() {
        GitHubEventDTO event = new GitHubEventDTO();
        event.setId("123");
        event.setType("PushEvent");
        ResponseEntity<List<GitHubEventDTO>> response = ResponseEntity.ok(List.of(event));
        when(githubRestTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(),
            any(ParameterizedTypeReference.class))).thenReturn(response);

        List<GitHubEventDTO> result = gitHubService.getUserEvents("testuser");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getType()).isEqualTo("PushEvent");
    }

    @Test
    void getUserEvents_userNotFound_returnsEmptyList() {
        when(githubRestTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(),
            any(ParameterizedTypeReference.class))).thenThrow(HttpClientErrorException.NotFound.class);
        assertThat(gitHubService.getUserEvents("ghost")).isEmpty();
    }
}