package com.opentrack.service;

import com.opentrack.model.Contributor;
import com.opentrack.model.ContributionEvent;
import com.opentrack.model.ContributionEvent.EventType;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class ScoringService {

    private static final Map<EventType, Integer> SCORE_MAP = Map.of(
        EventType.COMMIT, 3,
        EventType.PULL_REQUEST_OPENED, 5,
        EventType.PULL_REQUEST_MERGED, 10,
        EventType.ISSUE_OPENED, 2,
        EventType.ISSUE_CLOSED, 4,
        EventType.CODE_REVIEW, 6,
        EventType.FORK, 1,
        EventType.RELEASE, 15
    );

    public int calculateScore(EventType eventType) {
        return SCORE_MAP.getOrDefault(eventType, 0);
    }

    public void updateScoreForEvent(Contributor contributor, ContributionEvent event) {
        contributor.setTotalScore(contributor.getTotalScore() + event.getScoreAwarded());
        switch (event.getEventType()) {
            case COMMIT -> contributor.setCommitCount(contributor.getCommitCount() + 1);
            case PULL_REQUEST_OPENED, PULL_REQUEST_MERGED -> contributor.setPrCount(contributor.getPrCount() + 1);
            case ISSUE_OPENED, ISSUE_CLOSED -> contributor.setIssueCount(contributor.getIssueCount() + 1);
            case CODE_REVIEW -> contributor.setReviewCount(contributor.getReviewCount() + 1);
            default -> { }
        }
    }

    public double calculateContributorQualityScore(Contributor contributor) {
        double total = contributor.getPrCount() * 10.0 + contributor.getReviewCount() * 6.0
            + contributor.getCommitCount() * 3.0 + contributor.getIssueCount() * 3.0;
        long count = contributor.getCommitCount() + contributor.getPrCount()
            + contributor.getIssueCount() + contributor.getReviewCount();
        return count > 0 ? total / count : 0.0;
    }
}