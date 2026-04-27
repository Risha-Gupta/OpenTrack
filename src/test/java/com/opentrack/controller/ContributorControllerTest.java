package com.opentrack.controller;

import com.opentrack.model.Contributor;
import com.opentrack.service.ContributorService;
import com.opentrack.service.LeaderboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContributorController.class)
class ContributorControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private ContributorService contributorService;
    @MockBean private LeaderboardService leaderboardService;

    @Test
    @WithMockUser
    void getContributor_existingUser_returns200() throws Exception {
        Contributor c = Contributor.builder().id(1L).githubUsername("dev-alice").totalScore(340)
            .commitCount(45).prCount(12).issueCount(8).reviewCount(15).build();
        when(contributorService.getByGithubUsername("dev-alice")).thenReturn(c);
        mockMvc.perform(get("/contributors/dev-alice"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.githubUsername").value("dev-alice"))
            .andExpect(jsonPath("$.totalScore").value(340));
    }

    @Test
    @WithMockUser
    void getLeaderboard_returnsPagedResults() throws Exception {
        Contributor c = Contributor.builder().githubUsername("dev-alice").totalScore(340).build();
        when(leaderboardService.getLeaderboard(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(c)));
        mockMvc.perform(get("/contributors?page=0&size=10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].githubUsername").value("dev-alice"));
    }
}