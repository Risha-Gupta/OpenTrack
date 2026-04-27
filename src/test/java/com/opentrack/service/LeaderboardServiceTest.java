package com.opentrack.service;

import com.opentrack.model.Contributor;
import com.opentrack.repository.ContributorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeaderboardServiceTest {

    @Mock private ContributorRepository contributorRepository;
    @InjectMocks private LeaderboardService leaderboardService;

    @Test
    void getLeaderboard_returnsSortedByScore() {
        Contributor c1 = Contributor.builder().githubUsername("alice").totalScore(100).build();
        Contributor c2 = Contributor.builder().githubUsername("bob").totalScore(50).build();
        when(contributorRepository.findAllByOrderByTotalScoreDesc(any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of(c1, c2)));

        Page<Contributor> result = leaderboardService.getLeaderboard(PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getTotalScore())
            .isGreaterThanOrEqualTo(result.getContent().get(1).getTotalScore());
    }

    @Test
    void getTopN_returnsCorrectCount() {
        List<Contributor> top3 = List.of(
            Contributor.builder().githubUsername("a").totalScore(300).build(),
            Contributor.builder().githubUsername("b").totalScore(200).build(),
            Contributor.builder().githubUsername("c").totalScore(100).build()
        );
        when(contributorRepository.findAllByOrderByTotalScoreDesc(any(Pageable.class)))
            .thenReturn(new PageImpl<>(top3));
        assertThat(leaderboardService.getTopN(3)).hasSize(3);
    }
}