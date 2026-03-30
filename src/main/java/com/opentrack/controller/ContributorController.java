package com.opentrack.controller;

import com.opentrack.model.Contributor;
import com.opentrack.service.ContributorService;
import com.opentrack.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/contributors")
@RequiredArgsConstructor
public class ContributorController {

    private final ContributorService contributorService;
    private final LeaderboardService leaderboardService;

    @GetMapping("/{username}")
    public ResponseEntity<Contributor> getContributor(@PathVariable String username) {
        return ResponseEntity.ok(contributorService.getByGithubUsername(username));
    }

    @GetMapping
    public ResponseEntity<Page<Contributor>> getAllContributors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(leaderboardService.getLeaderboard(PageRequest.of(page, size)));
    }

    @GetMapping("/{username}/stats")
    public ResponseEntity<?> getStats(@PathVariable String username) {
        Contributor c = contributorService.getByGithubUsername(username);
        return ResponseEntity.ok(Map.of(
            "githubUsername", c.getGithubUsername(), "totalScore", c.getTotalScore(),
            "commitCount", c.getCommitCount(), "prCount", c.getPrCount(),
            "issueCount", c.getIssueCount(), "reviewCount", c.getReviewCount(),
            "lastSynced", c.getLastSyncedAt()
        ));
    }
}
