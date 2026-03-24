package com.opentrack.service;

import com.opentrack.config.GitHubApiConfig;
import com.opentrack.dto.GitHubEventDTO;
import com.opentrack.dto.GitHubRepoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubService {

    private final RestTemplate githubRestTemplate;
    private final GitHubApiConfig gitHubApiConfig;

    @Value("${github.api.rate-limit-pause-ms}")
    private long rateLimitPauseMs;

    @Cacheable(value = "github-events", key = "#username")
    public List<GitHubEventDTO> getUserEvents(String username) {
        String url = gitHubApiConfig.getBaseUrl() + "/users/" + username + "/events?per_page=100";
        try {
            ResponseEntity<List<GitHubEventDTO>> response = githubRestTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("GitHub user not found: {}", username);
            return Collections.emptyList();
        } catch (HttpClientErrorException.Forbidden e) {
            log.warn("GitHub API rate limit hit for: {}. Pausing {}ms", username, rateLimitPauseMs);
            pauseForRateLimit();
            return Collections.emptyList();
        }
    }

    @Cacheable(value = "github-repos", key = "#username")
    public List<GitHubRepoDTO> getUserRepos(String username) {
        String url = gitHubApiConfig.getBaseUrl() + "/users/" + username + "/repos?per_page=100&sort=updated";
        try {
            ResponseEntity<List<GitHubRepoDTO>> response = githubRestTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (HttpClientErrorException e) {
            log.error("Error fetching repos for {}: {}", username, e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<GitHubEventDTO> getOrgEvents(String orgName) {
        String url = gitHubApiConfig.getBaseUrl() + "/orgs/" + orgName + "/events?per_page=100";
        try {
            ResponseEntity<List<GitHubEventDTO>> response = githubRestTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (HttpClientErrorException e) {
            log.error("Error fetching org events for {}: {}", orgName, e.getMessage());
            return Collections.emptyList();
        }
    }

    private void pauseForRateLimit() {
        try { Thread.sleep(rateLimitPauseMs); }
        catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
    }
}