package org.fmarin.admintournoi.admin.match;

import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.TeamBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MatchUTest {

    @Test
    public void getStatus_should_return_DRAW_when_team_does_not_play_match() {
        // Given
        Match match = MatchBuilder.aMatch().build();

        Team team = TeamBuilder.aTeam().build();

        // When
        MatchStatus result = match.getStatus(team);

        // Then
        assertThat(result).isEqualTo(MatchStatus.DRAW);
    }

    @Test
    public void getStatus_should_return_VICTORY() {
        // Given
        Team winner = TeamBuilder.aTeam().withId(1L).build();
        Team loser = TeamBuilder.aTeam().withId(2L).build();
        Match match = MatchBuilder.aMatch()
                .withTeam1(winner)
                .withScoreTeam1(21)
                .withTeam2(loser)
                .withScoreTeam2(15)
                .build();

        // When
        MatchStatus win = match.getStatus(winner);
        MatchStatus lose = match.getStatus(loser);

        // Then
        assertThat(win).isEqualTo(MatchStatus.VICTORY);
        assertThat(lose).isEqualTo(MatchStatus.DEFEAT);
    }

    @Test
    public void getStatus_should_return_DEFEAT() {
        // Given
        Team winner = TeamBuilder.aTeam().withId(1L).build();
        Team loser = TeamBuilder.aTeam().withId(2L).build();
        Match match = MatchBuilder.aMatch()
                .withTeam1(loser)
                .withScoreTeam1(15)
                .withTeam2(winner)
                .withScoreTeam2(21)
                .build();


        // When
        MatchStatus win = match.getStatus(winner);
        MatchStatus lose = match.getStatus(loser);

        // Then
        assertThat(win).isEqualTo(MatchStatus.VICTORY);
        assertThat(lose).isEqualTo(MatchStatus.DEFEAT);
    }
}