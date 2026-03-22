package com.opentrack.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "contributors")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Contributor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "github_username", nullable = false, unique = true, length = 50)
    private String githubUsername;

    @Column(name = "display_name", length = 100)
    private String displayName;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "github_profile_url", length = 500)
    private String githubProfileUrl;

    @Column(name = "total_score", nullable = false)
    private Integer totalScore = 0;

    @Column(name = "commit_count")
    private Integer commitCount = 0;

    @Column(name = "pr_count")
    private Integer prCount = 0;

    @Column(name = "issue_count")
    private Integer issueCount = 0;

    @Column(name = "review_count")
    private Integer reviewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(name = "last_synced_at")
    private LocalDateTime lastSyncedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}