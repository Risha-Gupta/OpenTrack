package com.opentrack.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GitHubApiConfig {

    @Value("${github.api.base-url}")
    private String baseUrl;

    @Value("${github.api.token}")
    private String token;

    @Bean
    public RestTemplate githubRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.set("Accept", "application/vnd.github.v3+json");
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-GitHub-Api-Version", "2022-11-28");
            return execution.execute(request, body);
        });
        return restTemplate;
    }

    public String getBaseUrl() { return baseUrl; }
}