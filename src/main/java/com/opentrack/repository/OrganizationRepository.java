package com.opentrack.repository;

import com.opentrack.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByGithubOrgName(String githubOrgName);
    boolean existsByGithubOrgName(String githubOrgName);
}