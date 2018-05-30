package org.fmarin.admintournoi.admin.pool;

import com.google.common.collect.Lists;
import org.fmarin.admintournoi.admin.match.Match;
import org.fmarin.admintournoi.admin.match.MatchBuilder;
import org.fmarin.admintournoi.admin.ranking.Ranking;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.TeamBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fmarin.admintournoi.admin.match.MatchBuilder.aMatch;
import static org.fmarin.admintournoi.admin.pool.PoolBuilder.aPool;
import static org.fmarin.admintournoi.admin.ranking.RankingBuilder.aRanking;
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

  @Test
  public void getPoolRanking() {
    // Given
    Team teamA = TeamBuilder.aTeam().withId(1L).build();
    Team teamB = TeamBuilder.aTeam().withId(2L).build();
    Team teamC = TeamBuilder.aTeam().withId(3L).build();

    Match match1 = aMatch()
      .withTeam1(teamA).withScoreTeam1(21)
      .withTeam2(teamB).withScoreTeam2(15)
      .build();
    Match match2 = aMatch()
      .withTeam1(teamA).withScoreTeam1(12)
      .withTeam2(teamC).withScoreTeam2(21)
      .build();
    Match match3 = aMatch()
      .withTeam1(teamC).withScoreTeam1(21)
      .withTeam2(teamB).withScoreTeam2(19)
      .build();

    Pool pool = aPool().withMatches(Lists.newArrayList(match1, match2, match3)).build();

    // When
    List<Ranking> result = pool.getRankings();

    // Then
    Ranking ranking1 = aRanking()
      .withPosition(1)
      .withTeam(teamC)
      .withVictories(2)
      .withDefeats(0)
      .withPointsFor(42)
      .withPointsAgainst(31)
      .withDifference(11)
      .build();
    Ranking ranking2 = aRanking()
      .withTeam(teamA)
      .withPosition(2)
      .withVictories(1)
      .withDefeats(1)
      .withPointsFor(33)
      .withPointsAgainst(36)
      .withDifference(-3)
      .build();
    Ranking ranking3 = aRanking()
      .withPosition(3)
      .withTeam(teamB)
      .withVictories(0)
      .withDefeats(2)
      .withPointsFor(34)
      .withPointsAgainst(42)
      .withDifference(-8)
      .build();
    assertThat(result).hasSize(3)
      .containsSequence(ranking1, ranking2, ranking3);
  }

  @Test
  public void isFinished_return_false_when_no_match() {
    // Given
    Pool pool = PoolBuilder.aPool().build();

    // When
    boolean result = pool.isFinished();

    // Then
    assertThat(result).isFalse();
  }

  @Test
  public void isFinished_return_false_when_a_match_is_not_finished() {
      // Given
    Pool pool = PoolBuilder.aPool()
      .withMatches(Lists.newArrayList(
        MatchBuilder.aMatch().withScoreTeam1(0).withScoreTeam2(0).build(),
        MatchBuilder.aMatch().withScoreTeam1(12).withScoreTeam2(25).build()
      ))
      .build();

      // When
      boolean result = pool.isFinished();

      // Then
      assertThat(result).isFalse();
  }

  @Test
  public void isFinished_return_true_when_all_matches_are_finished() {
    // Given
    Pool pool = PoolBuilder.aPool()
      .withMatches(Lists.newArrayList(
        MatchBuilder.aMatch().withScoreTeam1(25).withScoreTeam2(10).build(),
        MatchBuilder.aMatch().withScoreTeam1(12).withScoreTeam2(25).build()
      ))
      .build();

    // When
    boolean result = pool.isFinished();

    // Then
    assertThat(result).isTrue();
  }
}