package com.opentrack.scheduler;

import com.opentrack.model.Contributor;
import com.opentrack.repository.ContributorRepository;
import com.opentrack.service.ContributorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GitHubSyncScheduler {

    private final ContributorRepository contributorRepository;
    private final ContributorService contributorService;

    @Scheduled(cron = "${opentrack.sync.cron}")
    public void runDailySync() {
        log.info("Starting daily GitHub contribution sync...");
        List<Contributor> all = contributorRepository.findAll();
        int success = 0, failed = 0;

        for (Contributor contributor : all) {
            try {
                contributorService.syncContributor(contributor.getGithubUsername());
                success++;
            } catch (Exception e) {
                log.error("Failed sync for: {}, error: {}", contributor.getGithubUsername(), e.getMessage());
                failed++;
            }
        }
        log.info("Daily sync done. Success: {}, Failed: {}", success, failed);
    }
}