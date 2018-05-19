package org.fmarin.admintournoi.admin.pool;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.admin.round.RoundBuilder;
import org.fmarin.admintournoi.admin.round.RoundRepository;
import org.fmarin.admintournoi.subscription.Level;
import org.fmarin.admintournoi.subscription.Team;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.fmarin.admintournoi.subscription.TeamBuilder.aTeam;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class PoolGenerationServiceUTest {

  private PoolGenerationService service;

  @Mock
  private RoundRepository mockedRoundRepository;

  private List<Team> teams = Lists.newArrayList(
    aTeam().withLevel(Level.NATIONAL).build(),
    aTeam().withLevel(Level.REGIONAL).build(),
    aTeam().withLevel(Level.NATIONAL).build(),
    aTeam().withLevel(Level.NATIONAL).build(),
    aTeam().withLevel(Level.LOISIRS).build(),
    aTeam().withLevel(Level.REGIONAL).build(),
    aTeam().withLevel(Level.DEPARTEMENTAL).build()
  );

  private Round firstRound;
  private Round secondRound;
  private Round lastRound;

  @Before
  public void setUp() {
    service = new PoolGenerationService(null, mockedRoundRepository, null);

    Team teamA = aTeam().withId(1L).build();
    Team teamB = aTeam().withId(2L).build();
    Team teamC = aTeam().withId(3L).build();
    Team teamD = aTeam().withId(4L).build();
    Team teamE = aTeam().withId(5L).build();
    Team teamF = aTeam().withId(6L).build();
    Team teamG = aTeam().withId(7L).build();
    Team teamH = aTeam().withId(8L).build();
    Team teamI = aTeam().withId(9L).build();

    List<Team> first = Lists.newArrayList(teamA, teamB, teamC, teamD, teamE, teamF, teamG, teamH, teamI);
    List<Team> second = Lists.newArrayList(teamA, teamB, teamC, teamD, teamE, teamF);

    firstRound = RoundBuilder.aRound()
      .withId(1L)
      .withFieldRanges("1-1")
      .withTeams(first)
      .withPools(Lists.newArrayList(
        PoolBuilder.aPool().withTeam1(teamA).withTeam2(teamD).withTeam3(teamG).build(),
        PoolBuilder.aPool().withTeam1(teamB).withTeam2(teamE).withTeam3(teamH).build(),
        PoolBuilder.aPool().withTeam1(teamC).withTeam2(teamF).withTeam3(teamI).build()
      ))
      .build();

    secondRound = RoundBuilder.aRound()
      .withId(2L)
      .withFieldRanges("1-1")
      .withPreviousRound(firstRound)
      .withTeams(second)
      .withPools(Lists.newArrayList(
        PoolBuilder.aPool().withTeam1(teamA).withTeam2(teamB).withTeam3(teamC).build(),
        PoolBuilder.aPool().withTeam1(teamD).withTeam2(teamE).withTeam3(teamF).build()
      ))
      .build();

    lastRound = RoundBuilder.aRound()
      .withId(3L)
      .withFieldRanges("1-1")
      .withPreviousRound(secondRound)
      .build();

    when(mockedRoundRepository.findOne(1L)).thenReturn(firstRound);
    when(mockedRoundRepository.findOne(2L)).thenReturn(secondRound);
    when(mockedRoundRepository.findOne(3L)).thenReturn(lastRound);
  }

  @Test
  public void orderLevels_with_all_levels() {
    // Given
    Set<Integer> levels = Sets.newHashSet(1, 2, 3, 4);
    Set<Integer> levelsOther = Sets.newHashSet(2, 4, 1, 3);

    // When
    List<Integer> result = service.orderLevels(levels, 1);
    List<Integer> resultOther = service.orderLevels(levelsOther, 1);

    // Then
    assertThat(result).containsSequence(1, 2, 3, 4);
    assertThat(resultOther).containsSequence(1, 2, 3, 4);
  }

  @Test
  public void orderLevels_with_last_pool_count() {
    // Given
    Set<Integer> levels = Sets.newHashSet(2, 1, 3);

    // When
    List<Integer> result = service.orderLevels(levels, 3);

    // Then
    assertThat(result).containsSequence(3, 2, 1);
  }

  @Test
  public void mapTeamsByLevel() {
    // Given
    // When
    Map<Integer, List<Team>> result = service.mapTeamsByLevel(teams);

    // Then
    assertThat(result).hasSize(4);
    assertThat(result.get(1)).hasSize(3);
    assertThat(result.get(2)).hasSize(2);
    assertThat(result.get(3)).hasSize(1);
    assertThat(result.get(4)).hasSize(1);
  }

  @Test
  public void getPool() {
    // Given
    List<Pool> pools = LongStream.range(0, 16).mapToObj(index -> PoolBuilder.aPool().withId(index).build()).collect(Collectors.toList());
    Round round = RoundBuilder.aRound().withFieldRanges("1-4").withPools(pools).build();

    // When
    Pool result0 = service.getPool(round, 0);
    Pool result16 = service.getPool(round, 15);
    Pool resultLoop = service.getPool(round, 24);

    // Then
    assertThat(result0).isEqualTo(round.getPools().get(0));
    assertThat(result16).isEqualTo(round.getPools().get(15));
    assertThat(resultLoop).isEqualTo(round.getPools().get(8));
  }

  @Test
  public void countTeamsByLevel() {
    // Given
    // When
    Map<Integer, Long> result = service.countTeamsByLevel(teams);

    // Then
    assertThat(result).hasSize(4)
      .containsExactly(
        entry(1, 3L),
        entry(2, 2L),
        entry(3, 1L),
        entry(4, 1L)
      );
  }

  @Test
  public void getPreviousOppositions_is_totally_empty_with_first_round() {
    // Given
    // When
    Set<TeamOpposition> result = service.getPreviousOppositions(firstRound);

    // Then
    assertThat(result).isEmpty();
  }

  @Test
  public void getPreviousOppositions_is_filled_with_already_opposed_team_with_one_previous_round() {
    // When
    Set<TeamOpposition> result = service.getPreviousOppositions(secondRound);

    // Then
    assertThat(result).hasSize(9);
  }

  @Test
  public void getPreviousOppositions_is_filled_with_already_opposed_team_with_multiple_previous_round() {
    // Given

    // When
    Set<TeamOpposition> result = service.getPreviousOppositions(lastRound);

    // Then
    assertThat(result).hasSize(15);
  }

  @Test
  public void hasAlreadyPlayedAgainst_with_empty_pool() {
    // Given
    Pool emptyPool = PoolBuilder.aPool().build();
    Team team = aTeam().withId(1L).build();
    Set<TeamOpposition> oppositions = Sets.newHashSet(
      new TeamOpposition(aTeam().withId(2L).build(), aTeam().withId(3L).build())
    );

    // When
    boolean result = service.hasAlreadyPlayedAgainst(team, emptyPool, oppositions);

    // Then
    assertThat(result).isFalse();
  }

  @Test
  public void hasAlreadyPlayedAgainst_with_empty_oppostions() {
    // Given
    Pool pool = PoolBuilder.aPool().withTeam1(aTeam().withId(2L).build()).build();
    Team team = aTeam().withId(1L).build();
    Set<TeamOpposition> oppositions = Sets.newHashSet();

    // When
    boolean result = service.hasAlreadyPlayedAgainst(team, pool, oppositions);

    // Then
    assertThat(result).isFalse();
  }

  @Test
  public void hasAlreadyPlayedAgainst_with_team_1_and_no_opposition() {
    // Given
    Pool pool = PoolBuilder.aPool().withTeam1(aTeam().withId(2L).build()).build();
    Team team = aTeam().withId(1L).build();
    Set<TeamOpposition> oppositions = Sets.newHashSet(new TeamOpposition(aTeam().withId(3L).build(), aTeam().withId(2L).build()));

    // When
    boolean result = service.hasAlreadyPlayedAgainst(team, pool, oppositions);

    // Then
    assertThat(result).isFalse();
  }

  @Test
  public void hasAlreadyPlayedAgainst_with_team_1_and_with_opposition() {
    // Given
    Pool pool = PoolBuilder.aPool().withTeam1(aTeam().withId(2L).build()).build();
    Team team = aTeam().withId(1L).build();
    Set<TeamOpposition> oppositions = Sets.newHashSet(new TeamOpposition(aTeam().withId(2L).build(), aTeam().withId(1L).build()));

    // When
    boolean result = service.hasAlreadyPlayedAgainst(team, pool, oppositions);

    // Then
    assertThat(result).isTrue();
  }

  @Test
  public void hasAlreadyPlayedAgainst_with_team_2_and_no_opposition() {
    // Given
    Pool pool = PoolBuilder.aPool().withTeam1(aTeam().withId(2L).build()).withTeam2(aTeam().withId(3L).build()).build();
    Team team = aTeam().withId(1L).build();
    Set<TeamOpposition> oppositions = Sets.newHashSet(new TeamOpposition(aTeam().withId(4L).build(), aTeam().withId(1L).build()));

    // When
    boolean result = service.hasAlreadyPlayedAgainst(team, pool, oppositions);

    // Then
    assertThat(result).isFalse();
  }

  @Test
  public void hasAlreadyPlayedAgainst_with_team_2_and_with_opposition() {
    // Given
    Pool pool = PoolBuilder.aPool().withTeam1(aTeam().withId(2L).build()).withTeam2(aTeam().withId(3L).build()).build();
    Team team = aTeam().withId(1L).build();
    Set<TeamOpposition> oppositions = Sets.newHashSet(new TeamOpposition(aTeam().withId(3L).build(), aTeam().withId(1L).build()));

    // When
    boolean result = service.hasAlreadyPlayedAgainst(team, pool, oppositions);

    // Then
    assertThat(result).isTrue();
  }
}