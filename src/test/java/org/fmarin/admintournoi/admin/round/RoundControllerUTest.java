package org.fmarin.admintournoi.admin.round;

import com.google.common.collect.Lists;
import org.fmarin.admintournoi.admin.match.MatchBuilder;
import org.fmarin.admintournoi.admin.pool.Pool;
import org.fmarin.admintournoi.subscription.Team;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fmarin.admintournoi.admin.pool.PoolBuilder.aPool;
import static org.fmarin.admintournoi.subscription.TeamBuilder.aTeam;

@RunWith(MockitoJUnitRunner.class)
public class RoundControllerUTest {

    @InjectMocks
    private RoundController controller;
    
    @Test
    public void getColorStatus_should_return_primary_when_there_is_not_match() {
        // Given
        Pool pool = aPool().build();
        
        // When
        String result = controller.getColorStatus(pool);

        // Then
        assertThat(result).isEqualTo("primary");
    }

    @Test
    public void getColorStatus_should_return_primary_when_there_is_not_finished_match() {
        // Given
        Pool pool = aPool()
                .withMatches(Lists.newArrayList(MatchBuilder.aMatch().build()))
                .build();

        // When
        String result = controller.getColorStatus(pool);

        // Then
        assertThat(result).isEqualTo("primary");
    }

    @Test
    public void getColorStatus_should_return_success_when_all_matches_are_finished() {
        // Given
        Pool pool = aPool()
                .withMatches(Lists.newArrayList(MatchBuilder.aMatch().withScoreTeam1(12).build()))
                .build();

        // When
        String result = controller.getColorStatus(pool);

        // Then
        assertThat(result).isEqualTo("success");
    }
    
    @Test
    public void switchTeams_when_team_is_team1() {
        // Given
        Team team1 = aTeam().withId(1L).build();
        Team team2 = aTeam().withId(2L).build();

        Pool pool = aPool()
                .withTeam1(team1)
                .build();
        
        // When
        controller.switchTeams(pool, team1, team2);
        
        // Then
        assertThat(pool.getTeam1()).isEqualTo(team2);
    }

    @Test
    public void switchTeams_when_team_is_team2() {
        // Given
        Team team1 = aTeam().withId(1L).build();
        Team team2 = aTeam().withId(2L).build();

        Pool pool = aPool()
                .withTeam2(team1)
                .build();

        // When
        controller.switchTeams(pool, team1, team2);

        // Then
        assertThat(pool.getTeam2()).isEqualTo(team2);
    }

    @Test
    public void switchTeams_when_team_is_team3() {
        // Given
        Team team1 = aTeam().withId(1L).build();
        Team team2 = aTeam().withId(2L).build();

        Pool pool = aPool()
                .withTeam3(team1)
                .build();

        // When
        controller.switchTeams(pool, team1, team2);

        // Then
        assertThat(pool.getTeam3()).isEqualTo(team2);
    }
    
}