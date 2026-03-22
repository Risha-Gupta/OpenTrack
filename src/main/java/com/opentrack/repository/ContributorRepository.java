package com.opentrack.repository;

import com.opentrack.model.Contributor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ContributorRepository extends JpaRepository<Contributor, Long> {
    Optional<Contributor> findByGithubUsername(String githubUsername);
    Page<Contributor> findAllByOrderByTotalScoreDesc(Pageable pageable);

    @Query("SELECT c FROM Contributor c WHERE c.organization.id = :orgId ORDER BY c.totalScore DESC")
    Page<Contributor> findByOrganizationIdOrderByScoreDesc(Long orgId, Pageable pageable);

    boolean existsByGithubUsername(String githubUsername);
}
