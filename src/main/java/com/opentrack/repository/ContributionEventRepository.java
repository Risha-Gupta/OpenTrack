package com.opentrack.repository;

import com.opentrack.model.ContributionEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContributionEventRepository extends JpaRepository<ContributionEvent, Long> {
    boolean existsByGithubEventId(String githubEventId);
    List<ContributionEvent> findByContributorIdOrderByOccurredAtDesc(Long contributorId);
    Page<ContributionEvent> findByContributorId(Long contributorId, Pageable pageable);

    @Query("SELECT c FROM ContributionEvent c WHERE c.contributor.id = :contributorId AND c.occurredAt BETWEEN :from AND :to")
    List<ContributionEvent> findByContributorIdAndDateRange(Long contributorId, LocalDateTime from, LocalDateTime to);

    @Query("SELECT c.eventType, COUNT(c) FROM ContributionEvent c WHERE c.contributor.id = :contributorId GROUP BY c.eventType")
    List<Object[]> countEventsByTypeForContributor(Long contributorId);
}