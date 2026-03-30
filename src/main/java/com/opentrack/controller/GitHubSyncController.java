package com.opentrack.controller;

import com.opentrack.model.Contributor;
import com.opentrack.service.ContributorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/sync")
@RequiredArgsConstructor
public class GitHubSyncController {

    private final ContributorService contributorService;

    @PostMapping("/contributor/{username}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> syncContributor(@PathVariable String username) {
        Contributor contributor = contributorService.syncContributor(username);
        return ResponseEntity.ok(Map.of(
            "message", "Sync completed for " + username,
            "totalScore", contributor.getTotalScore(),
            "lastSynced", contributor.getLastSyncedAt()
        ));
    }

    @PostMapping("/org/{orgName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> syncOrganization(@PathVariable String orgName) {
        return ResponseEntity.accepted().body(Map.of(
            "message", "Organization sync queued for: " + orgName, "status", "QUEUED"));
    }
}