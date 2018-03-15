package org.fmarin.admintournoi.admin;

import com.google.common.collect.Lists;
import org.fmarin.admintournoi.helper.TimeMachine;
import org.fmarin.admintournoi.subscription.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DashboardServiceUTest {

    private DashboardService dashboardService;

    @Mock
    private TournamentRepository mockedTournamentRepository;

    @Before
    public void setUp() {
        dashboardService = new DashboardService(mockedTournamentRepository);
    }

    @Test
    public void getCurrentTournament_should_return_empty_map_without_data() {
        // When
        Map<String, Object> result = dashboardService.getCurrentSubscriptionsStats(Gender.MEN);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    public void getCurrentTournament() {
        // Given
        TimeMachine.useFixedClockAt(LocalDateTime.of(2017, 1, 1, 0, 0));

        Tournament tournament = TournamentBuilder.aTournament()
                .withId(1L)
                .withTeamLimit(12)
                .withTeams(Lists.newArrayList(
                        TeamBuilder.aTeam().withPaymentStatus("Completed").build(),
                        TeamBuilder.aTeam().withPaymentStatus("In Progress").build(),
                        TeamBuilder.aTeam().withPaymentStatus("Completed").build()))
                .build();
        when(mockedTournamentRepository.findByYearAndGender(2017, Gender.MEN)).thenReturn(tournament);

        // When
        Map<String, Object> result = dashboardService.getCurrentSubscriptionsStats(Gender.MEN);

        // Then
        assertThat(result).containsOnly(
                entry("men_teams_limit", 12),
                entry("men_teams_subscribed", 2L)
        );
    }


}