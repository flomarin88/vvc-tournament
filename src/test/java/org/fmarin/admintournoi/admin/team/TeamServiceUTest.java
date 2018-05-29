package org.fmarin.admintournoi.admin.team;

import org.assertj.core.util.Lists;
import org.fmarin.admintournoi.admin.round.PreviousRound;
import org.fmarin.admintournoi.admin.round.PreviousRoundBuilder;
import org.fmarin.admintournoi.admin.round.Round;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fmarin.admintournoi.admin.round.RoundBuilder.aRound;

@RunWith(MockitoJUnitRunner.class)
public class TeamServiceUTest {

  @InjectMocks
  private TeamService service;

  @Test
  public void getAllPreviousRounds_empty() {
    // Given
    Round first = aRound().build();

    // When
    List<Round> result = service.getAllPreviousRounds(first);

    // Then
    assertThat(result).isEmpty();
  }

  @Test
  public void getAllPreviousRounds_with_multiple_level() {
    // Given
    Round first = aRound().build();
    PreviousRound previousRoundFirst = PreviousRoundBuilder.aPreviousRound().withPreviousRound(first).build();
    Round second = aRound().withPreviousRounds(Lists.newArrayList(previousRoundFirst)).build();
    PreviousRound previousRoundSecond = PreviousRoundBuilder.aPreviousRound().withPreviousRound(second).build();
    Round round = aRound().withPreviousRounds(Lists.newArrayList(previousRoundSecond)).build();

    // When
    List<Round> result = service.getAllPreviousRounds(round);

    // Then
    assertThat(result).hasSize(2).contains(first, second);
  }

  @Test
  public void getAllPreviousRounds_with_multiple_at_same_level() {
    // Given
    Round first = aRound().build();
    PreviousRound previousRoundFirst = PreviousRoundBuilder.aPreviousRound().withPreviousRound(first).build();
    Round second = aRound().withPreviousRounds(Lists.newArrayList(previousRoundFirst)).build();
    PreviousRound previousRoundSecond = PreviousRoundBuilder.aPreviousRound().withPreviousRound(second).build();
    Round third = aRound().build();
    PreviousRound previousRoundSecond2 = PreviousRoundBuilder.aPreviousRound().withPreviousRound(third).build();
    Round round = aRound().withPreviousRounds(Lists.newArrayList(previousRoundSecond, previousRoundSecond2)).build();

    // When
    List<Round> result = service.getAllPreviousRounds(round);

    // Then
    assertThat(result).hasSize(3).contains(first, second, third);
  }
}