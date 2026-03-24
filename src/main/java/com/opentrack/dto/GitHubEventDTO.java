package com.opentrack.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubEventDTO {
    private String id;
    private String type;

    @JsonProperty("actor")
    private Actor actor;

    @JsonProperty("repo")
    private Repo repo;

    @JsonProperty("payload")
    private Map<String, Object> payload;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Actor {
        private Long id;
        private String login;
        @JsonProperty("avatar_url")
        private String avatarUrl;
        private String url;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Repo {
        private Long id;
        private String name;
        private String url;
    }
}