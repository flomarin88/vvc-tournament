package org.fmarin.admintournoi.admin.round;

import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.TeamBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RoundUTest {

  private Round round = RoundBuilder.aRound().withFieldRanges("4-11;20-22").build();

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
  public void createPools() {
    // Given
    List<Team> teams = IntStream.range(0, 13).mapToObj(index -> TeamBuilder.aTeam().build()).collect(Collectors.toList());
    round.setTeams(teams);

    // When
    round.createPools();

    // Then
    assertThat(round.getPools()).hasSize(4);
    assertThat(round.getPools().get(0))
      .extracting("field", "position", "round")
      .contains(4, 1, round);
  }
}