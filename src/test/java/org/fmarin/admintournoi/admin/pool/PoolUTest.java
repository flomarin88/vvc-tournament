package org.fmarin.admintournoi.admin.pool;

import org.fmarin.admintournoi.subscription.Team;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fmarin.admintournoi.admin.pool.PoolBuilder.aPool;
import static org.fmarin.admintournoi.subscription.TeamBuilder.aTeam;

@RunWith(MockitoJUnitRunner.class)
public class PoolUTest {

  @Test
  public void replace_when_team_is_team1() {
    // Given
    Team team1 = aTeam().withId(1L).build();
    Team team2 = aTeam().withId(2L).build();

    Pool pool = aPool()
      .withTeam1(team1)
      .build();

    // When
    pool.replace(team1, team2);

    // Then
    assertThat(pool.getTeam1()).isEqualTo(team2);
  }

  @Test
  public void replace_when_team_is_team2() {
    // Given
    Team team1 = aTeam().withId(1L).build();
    Team team2 = aTeam().withId(2L).build();

    Pool pool = aPool()
      .withTeam2(team1)
      .build();

    // When
    pool.replace(team1, team2);

    // Then
    assertThat(pool.getTeam2()).isEqualTo(team2);
  }

  @Test
  public void replace_when_team_is_team3() {
    // Given
    Team team1 = aTeam().withId(1L).build();
    Team team2 = aTeam().withId(2L).build();

    Pool pool = aPool()
      .withTeam3(team1)
      .build();

    // When
    pool.replace(team1, team2);

    // Then
    assertThat(pool.getTeam3()).isEqualTo(team2);
  }
}