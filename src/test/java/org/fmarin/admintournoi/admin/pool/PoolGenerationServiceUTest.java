package org.fmarin.admintournoi.admin.pool;

import com.google.common.collect.Lists;
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
    Round round = RoundBuilder.aRound().withPools(pools).build();

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
  public void getFieldList() {
    // Given
    // When
    List<Integer> result = service.getFieldList("4-11;20-22");

    // Then
    assertThat(result).hasSize(11)
      .containsSequence(4, 5, 6, 7, 8, 9, 10, 11, 20, 21, 22);
  }

  @Test
  public void getField() {
    // Given
    List<Integer> fields = Lists.newArrayList(4, 5, 6, 7, 8, 9, 10, 11);

    // When
    Integer result1 = service.getField(fields, 1);
    Integer result2 = service.getField(fields, 3);
    Integer result3 = service.getField(fields, 8);
    Integer result4 = service.getField(fields, 12);
    Integer result5 = service.getField(fields, 16);
    Integer result6 = service.getField(fields, 9);

    // Then
    assertThat(result1).isEqualTo(4);
    assertThat(result2).isEqualTo(6);
    assertThat(result3).isEqualTo(11);
    assertThat(result4).isEqualTo(7);
    assertThat(result5).isEqualTo(11);
    assertThat(result6).isEqualTo(4);
  }

}