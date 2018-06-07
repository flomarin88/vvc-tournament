package org.fmarin.admintournoi.admin.round;

import com.google.common.collect.Lists;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.TeamBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fmarin.admintournoi.admin.round.PreviousRoundBuilder.aPreviousRound;
import static org.fmarin.admintournoi.admin.round.RoundBuilder.aRound;

@RunWith(MockitoJUnitRunner.class)
public class RoundUTest {

  private Round round = aRound().withFieldRanges("4-11;20-22").build();

  @Test
  public void getFields() {
    // Given
    // When
    List<Integer> result = round.getFields();

    // Then
    assertThat(result).hasSize(11)
      .containsSequence(4, 5, 6, 7, 8, 9, 10, 11, 20, 21, 22);
  }

  @Test
  public void getField() {
    // Given
    // When
    Integer result1 = round.getField(1);
    Integer result2 = round.getField(3);
    Integer result3 = round.getField(8);
    Integer result4 = round.getField(12);
    Integer result5 = round.getField(16);
    Integer result6 = round.getField(22);

    // Then
    assertThat(result1).isEqualTo(4);
    assertThat(result2).isEqualTo(6);
    assertThat(result3).isEqualTo(11);
    assertThat(result4).isEqualTo(4);
    assertThat(result5).isEqualTo(8);
    assertThat(result6).isEqualTo(22);
  }

  @Test
  public void createPools_for_pool_type() {
    // Given
    List<Team> teams = IntStream.range(0, 13).mapToObj(index -> TeamBuilder.aTeam().build()).collect(Collectors.toList());
    round.setTeams(teams);
    round.setType(RoundType.POOL);

    // When
    round.createPools();

    // Then
    assertThat(round.getPools()).hasSize(4);
    assertThat(round.getPools().get(0))
      .extracting("field", "position", "round")
      .contains(4, 1, round);
  }

  @Test
  public void createPools_for_direct_elimination_type() {
    // Given
    List<Team> teams = IntStream.range(0, 16).mapToObj(index -> TeamBuilder.aTeam().build()).collect(Collectors.toList());
    round.setTeams(teams);
    round.setType(RoundType.DIRECT_ELIMINATION);

    // When
    round.createPools();

    // Then
    assertThat(round.getPools()).hasSize(8);
    assertThat(round.getPools().get(0))
      .extracting("field", "position", "round")
      .contains(4, 1, round);
  }

  @Test
  public void getLevel_returns_0_without_previous_round() {
    // Given
    Round round = aRound().build();

    // When
    Integer result = round.getLevel();

    // Then
    assertThat(result).isEqualTo(0);
  }

  @Test
  public void getLevel_returns_1_with_one_previous_round() {
    // Given
    Round round = aRound().withPreviousRounds(Lists.newArrayList(
      aPreviousRound().withPreviousRound(aRound().build()).build()
    )).build();

    // When
    Integer result = round.getLevel();

    // Then
    assertThat(result).isEqualTo(1);
  }

  @Test
  public void getLevel_returns_2_with_2_previous_round() {
    // Given
    Round round = aRound().withPreviousRounds(Lists.newArrayList(
      aPreviousRound().withPreviousRound(aRound().withPreviousRounds(Lists.newArrayList(
        aPreviousRound().withPreviousRound(aRound().build()).build()
      )).build()).build()
    )).build();

    // When
    Integer result = round.getLevel();

    // Then
    assertThat(result).isEqualTo(2);
  }
}