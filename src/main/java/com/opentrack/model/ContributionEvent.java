package com.opentrack.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "contribution_events")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ContributionEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contributor_id", nullable = false)
    private Contributor contributor;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @Column(name = "repo_name", length = 200)
    private String repoName;

    @Column(name = "github_event_id", unique = true, length = 100)
    private String githubEventId;

    @Column(name = "event_url", length = 500)
    private String eventUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "score_awarded")
    private Integer scoreAwarded = 0;

    @Column(name = "occurred_at")
    private LocalDateTime occurredAt;

    @CreationTimestamp
    @Column(name = "recorded_at", updatable = false)
    private LocalDateTime recordedAt;

    public enum EventType {
        COMMIT, PULL_REQUEST_OPENED, PULL_REQUEST_MERGED,
        ISSUE_OPENED, ISSUE_CLOSED, CODE_REVIEW,
        FORK, STAR, RELEASE
    }
}