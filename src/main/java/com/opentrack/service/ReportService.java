package com.opentrack.service;

import com.opentrack.model.Contributor;
import com.opentrack.model.ContributionEvent;
import com.opentrack.repository.ContributionEventRepository;
import com.opentrack.repository.ContributorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ContributorRepository contributorRepository;
    private final ContributionEventRepository eventRepository;

    public Map<String, Object> generateMonthlyReport(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDateTime from = ym.atDay(1).atStartOfDay();
        LocalDateTime to = ym.atEndOfMonth().atTime(23, 59, 59);

        List<Contributor> allContributors = contributorRepository.findAll();
        Map<String, Integer> scoreByContributor = new LinkedHashMap<>();

        for (Contributor c : allContributors) {
            List<ContributionEvent> events = eventRepository.findByContributorIdAndDateRange(c.getId(), from, to);
            int score = events.stream().mapToInt(ContributionEvent::getScoreAwarded).sum();
            if (score > 0) scoreByContributor.put(c.getGithubUsername(), score);
        }

        List<Map.Entry<String, Integer>> sorted = scoreByContributor.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .collect(Collectors.toList());

        return Map.of("period", ym.toString(), "totalContributors", sorted.size(),
            "leaderboard", sorted, "generatedAt", LocalDateTime.now());
    }

    public Map<String, Object> generateWeeklyReport(String githubUsername) {
        LocalDateTime from = LocalDateTime.now().minusDays(7);
        LocalDateTime to = LocalDateTime.now();

        Contributor contributor = contributorRepository.findByGithubUsername(githubUsername)
            .orElseThrow(() -> new RuntimeException("Contributor not found: " + githubUsername));

        List<ContributionEvent> events = eventRepository
            .findByContributorIdAndDateRange(contributor.getId(), from, to);

        Map<String, Long> byType = events.stream()
            .collect(Collectors.groupingBy(e -> e.getEventType().name(), Collectors.counting()));

        return Map.of("githubUsername", githubUsername,
            "weekScore", events.stream().mapToInt(ContributionEvent::getScoreAwarded).sum(),
            "eventsByType", byType, "totalEvents", events.size(), "from", from, "to", to);
    }
}