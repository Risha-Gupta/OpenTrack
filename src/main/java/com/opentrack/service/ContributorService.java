package com.opentrack.service;

import com.opentrack.dto.GitHubEventDTO;
import com.opentrack.exception.ResourceNotFoundException;
import com.opentrack.model.Contributor;
import com.opentrack.model.ContributionEvent;
import com.opentrack.repository.ContributorRepository;
import com.opentrack.repository.ContributionEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContributorService {

    private final ContributorRepository contributorRepository;
    private final ContributionEventRepository eventRepository;
    private final GitHubService gitHubService;
    private final ScoringService scoringService;

    @Transactional
    public Contributor syncContributor(String githubUsername) {
        Contributor contributor = contributorRepository.findByGithubUsername(githubUsername)
            .orElseGet(() -> {
                log.info("Creating new contributor: {}", githubUsername);
                return Contributor.builder().githubUsername(githubUsername)
                    .commitCount(0).prCount(0).issueCount(0).reviewCount(0).totalScore(0).build();
            });

        List<GitHubEventDTO> events = gitHubService.getUserEvents(githubUsername);
        int newEventsCount = 0;

        for (GitHubEventDTO event : events) {
            if (!eventRepository.existsByGithubEventId(event.getId())) {
                ContributionEvent ce = mapEventToEntity(event, contributor);
                if (ce != null) {
                    eventRepository.save(ce);
                    scoringService.updateScoreForEvent(contributor, ce);
                    newEventsCount++;
                }
            }
        }

        contributor.setLastSyncedAt(LocalDateTime.now());
        Contributor saved = contributorRepository.save(contributor);
        log.info("Synced {} new events for: {}", newEventsCount, githubUsername);
        return saved;
    }

    public Contributor getByGithubUsername(String username) {
        return contributorRepository.findByGithubUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Contributor not found: " + username));
    }

    private ContributionEvent mapEventToEntity(GitHubEventDTO dto, Contributor contributor) {
        ContributionEvent.EventType eventType = switch (dto.getType()) {
            case "PushEvent" -> ContributionEvent.EventType.COMMIT;
            case "PullRequestEvent" -> {
                Object action = dto.getPayload().get("action");
                yield "closed".equals(action) ? ContributionEvent.EventType.PULL_REQUEST_MERGED
                    : ContributionEvent.EventType.PULL_REQUEST_OPENED;
            }
            case "IssuesEvent" -> ContributionEvent.EventType.ISSUE_OPENED;
            case "PullRequestReviewEvent" -> ContributionEvent.EventType.CODE_REVIEW;
            case "ForkEvent" -> ContributionEvent.EventType.FORK;
            default -> null;
        };
        if (eventType == null) return null;

        return ContributionEvent.builder()
            .contributor(contributor)
            .eventType(eventType)
            .repoName(dto.getRepo() != null ? dto.getRepo().getName() : "")
            .githubEventId(dto.getId())
            .occurredAt(dto.getCreatedAt())
            .scoreAwarded(scoringService.calculateScore(eventType))
            .build();
    }
}