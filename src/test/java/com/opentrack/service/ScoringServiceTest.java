package com.opentrack.service;

import com.opentrack.model.Contributor;
import com.opentrack.model.ContributionEvent;
import com.opentrack.model.ContributionEvent.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ScoringServiceTest {

    private ScoringService scoringService;

    @BeforeEach
    void setUp() { scoringService = new ScoringService(); }

    @Test
    void calculateScore_commit_returns3() {
        assertThat(scoringService.calculateScore(EventType.COMMIT)).isEqualTo(3);
    }

    @Test
    void calculateScore_prMerged_returns10() {
        assertThat(scoringService.calculateScore(EventType.PULL_REQUEST_MERGED)).isEqualTo(10);
    }

    @Test
    void calculateScore_release_returns15() {
        assertThat(scoringService.calculateScore(EventType.RELEASE)).isEqualTo(15);
    }

    @Test
    void updateScoreForEvent_incrementsCommitCount() {
        Contributor c = Contributor.builder().githubUsername("testuser")
            .totalScore(0).commitCount(0).prCount(0).issueCount(0).reviewCount(0).build();
        ContributionEvent event = ContributionEvent.builder()
            .eventType(EventType.COMMIT).scoreAwarded(3).build();
        scoringService.updateScoreForEvent(c, event);
        assertThat(c.getTotalScore()).isEqualTo(3);
        assertThat(c.getCommitCount()).isEqualTo(1);
    }

    @Test
    void updateScoreForEvent_prMerged_incrementsPrCount() {
        Contributor c = Contributor.builder().githubUsername("testuser")
            .totalScore(5).commitCount(2).prCount(0).issueCount(0).reviewCount(0).build();
        ContributionEvent event = ContributionEvent.builder()
            .eventType(EventType.PULL_REQUEST_MERGED).scoreAwarded(10).build();
        scoringService.updateScoreForEvent(c, event);
        assertThat(c.getTotalScore()).isEqualTo(15);
        assertThat(c.getPrCount()).isEqualTo(1);
    }

    @Test
    void qualityScore_nonZeroContributor_returnsPositive() {
        Contributor c = Contributor.builder().githubUsername("testuser").totalScore(100)
            .commitCount(10).prCount(5).issueCount(5).reviewCount(5).build();
        assertThat(scoringService.calculateContributorQualityScore(c)).isGreaterThan(0);
    }
}