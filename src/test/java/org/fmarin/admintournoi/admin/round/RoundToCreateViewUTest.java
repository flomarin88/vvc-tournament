package org.fmarin.admintournoi.admin.round;

import com.google.common.collect.Range;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RoundToCreateViewUTest {

  @Test
  public void getPreviousRounds_returns_empty_list_without_any_previous_round() {
    // Given
    RoundToCreateView round = new RoundToCreateView();

    // When
    List<PreviousRoundView> result = round.getPreviousRounds();

    // Then
    assertThat(result).isEmpty();
  }

  @Test
  public void getPreviousRounds_returns_1_element() {
    // Given
    RoundToCreateView round = new RoundToCreateView();
    round.setFirstPreviousRoundId(1L);
    round.setFirstTeamsFrom(4);
    round.setFirstTeamsTo(15);

    // When
    List<PreviousRoundView> result = round.getPreviousRounds();

    // Then
    assertThat(result)
      .hasSize(1)
      .containsExactly(new PreviousRoundView(1L, Range.closed(4, 15)));
  }

  @Test
  public void getPreviousRounds_returns_2_elements() {
    // Given
    RoundToCreateView round = new RoundToCreateView();
    round.setFirstPreviousRoundId(1L);
    round.setFirstTeamsFrom(4);
    round.setFirstTeamsTo(15);
    round.setSecondPreviousRoundId(2L);
    round.setSecondTeamsFrom(1);
    round.setSecondTeamsTo(3);

    // When
    List<PreviousRoundView> result = round.getPreviousRounds();

    // Then
    assertThat(result)
      .hasSize(2)
      .containsExactly(
        new PreviousRoundView(1L, Range.closed(4, 15)),
        new PreviousRoundView(2L, Range.closed(1, 3))
      );
  }

}