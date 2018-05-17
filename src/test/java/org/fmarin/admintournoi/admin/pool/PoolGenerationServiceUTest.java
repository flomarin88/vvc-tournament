package org.fmarin.admintournoi.admin.pool;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.admin.round.RoundBuilder;
import org.fmarin.admintournoi.subscription.Level;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.TeamBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;


@RunWith(MockitoJUnitRunner.class)
public class PoolGenerationServiceUTest {

  @InjectMocks
  private PoolGenerationService service;

  private List<Team> teams = Lists.newArrayList(
    TeamBuilder.aTeam().withLevel(Level.NATIONAL).build(),
    TeamBuilder.aTeam().withLevel(Level.REGIONAL).build(),
    TeamBuilder.aTeam().withLevel(Level.NATIONAL).build(),
    TeamBuilder.aTeam().withLevel(Level.NATIONAL).build(),
    TeamBuilder.aTeam().withLevel(Level.LOISIRS).build(),
    TeamBuilder.aTeam().withLevel(Level.REGIONAL).build(),
    TeamBuilder.aTeam().withLevel(Level.DEPARTEMENTAL).build()
  );


  @Test
  public void orderLevels_with_all_levels() {
    // Given
    Set<Integer> levels = Sets.newHashSet(1, 2, 3, 4);
    Set<Integer> levelsOther = Sets.newHashSet( 2, 4, 1, 3);

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

}