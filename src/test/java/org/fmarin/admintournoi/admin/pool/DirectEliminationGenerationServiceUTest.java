package org.fmarin.admintournoi.admin.pool;

import org.assertj.core.util.Lists;
import org.fmarin.admintournoi.admin.ranking.Ranking;
import org.fmarin.admintournoi.admin.ranking.RankingBuilder;
import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.admin.round.RoundRepository;
import org.fmarin.admintournoi.admin.round.RoundType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.fmarin.admintournoi.admin.round.PreviousRoundBuilder.aPreviousRound;
import static org.fmarin.admintournoi.admin.round.RoundBuilder.aRound;
import static org.fmarin.admintournoi.subscription.TeamBuilder.aTeam;

@RunWith(MockitoJUnitRunner.class)
public class DirectEliminationGenerationServiceUTest {

  private DirectEliminationGenerationService service;

  @Mock
  private RoundRepository mockedRoundRepository;

  private List<Pool> pools;
  private List<Ranking> rankings;

  @Before
  public void setUp() {
    service = new DirectEliminationGenerationService(mockedRoundRepository);
  }

  @Test
  public void affectTeams_semi() {
    // Given
    pools = createPools(2);
    rankings = createRankings(4);

    Round round = createRound(pools);

    // When
    service.affectTeams(round, rankings);

    // Then
    assertThat(round.getPools()).extracting("position", "team1.id", "team2.id")
      .contains(
        tuple(1, 1L, 4L),
        tuple(2, 2L, 3L));
  }

  @Test
  public void affectTeams_quarter() {
    // Given
    pools = createPools(4);
    rankings = createRankings(8);

    Round round = createRound(pools);

    // When
    service.affectTeams(round, rankings);

    // Then
    assertThat(round.getPools()).extracting("position", "team1.id", "team2.id")
      .contains(
        tuple(1, 1L, 8L), // 0
        tuple(2, 4L, 5L), // 3
        tuple(3, 3L, 6L), // 2
        tuple(4, 2L, 7L)); // 1
  }

  @Test
  public void affectTeams_8() {
    // Given
    pools = createPools(8);
    rankings = createRankings(16);

    Round round = createRound(pools);

    // When
    service.affectTeams(round, rankings);

    // Then
    assertThat(round.getPools()).extracting("position", "team1.id", "team2.id")
      .contains(
        tuple(1, 1L, 16L), // 0
        tuple(2, 8L, 9L), // 7
        tuple(3, 5L, 12L), // 4
        tuple(4, 4L, 13L), // 3
        tuple(5, 3L, 14L), // 2
        tuple(6, 6L, 11L), // 5
        tuple(7, 7L, 10L), // 6
        tuple(8, 2L, 15L)); // 1
  }

  private Round createRound(List<Pool> pools) {
    return aRound()
      .withType(RoundType.DIRECT_ELIMINATION)
      .withPreviousRounds(Lists.newArrayList(
        aPreviousRound().withPreviousRound(aRound().withType(RoundType.POOL).build()).build()))
      .withPools(pools)
      .build();
  }

  private List<Pool> createPools(Integer poolsCount) {
    return IntStream.range(0, poolsCount).mapToObj(index ->
      PoolBuilder.aPool()
        .withPosition(index + 1)
        .build()
    ).collect(Collectors.toList());
  }

  private List<Ranking> createRankings(Integer teamsCount) {
    return IntStream.range(0, teamsCount).mapToObj(index ->
      RankingBuilder.aRanking()
        .withPosition(index + 1)
        .withTeam(aTeam().withId((long) index + 1).build())
        .build()
    ).collect(Collectors.toList());
  }
}