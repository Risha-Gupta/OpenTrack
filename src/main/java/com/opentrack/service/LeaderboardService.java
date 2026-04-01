package com.opentrack.service;

import com.opentrack.model.Contributor;
import com.opentrack.repository.ContributorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final ContributorRepository contributorRepository;

    @Cacheable(value = "leaderboard", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<Contributor> getLeaderboard(Pageable pageable) {
        return contributorRepository.findAllByOrderByTotalScoreDesc(pageable);
    }

    @Cacheable(value = "org-leaderboard", key = "#orgId + '-' + #pageable.pageNumber")
    public Page<Contributor> getOrgLeaderboard(Long orgId, Pageable pageable) {
        return contributorRepository.findByOrganizationIdOrderByScoreDesc(orgId, pageable);
    }

    public List<Contributor> getTopN(int n) {
        return contributorRepository.findAllByOrderByTotalScoreDesc(Pageable.ofSize(n)).getContent();
    }
}