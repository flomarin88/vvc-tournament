package org.fmarin.admintournoi.subscription;

import org.fmarin.admintournoi.helper.TimeMachine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TournamentUTest {

  private LocalDateTime firstOfMay6Pm = LocalDateTime.of(2018, 5, 1, 18, 0, 0);
  private LocalDateTime tenthOfJuneMidnight = LocalDateTime.of(2018, 6, 10, 0, 0, 0);

  private Tournament tournament =
    TournamentBuilder.aTournament()
      .subscriptionsOpenAt(firstOfMay6Pm)
      .subscriptionsCloseAt(tenthOfJuneMidnight)
      .build();

  @Test
  public void areSubscriptionsOpened_should_return_false_before_First_of_May_6_PM() {
    // Given
    LocalDateTime now = LocalDateTime.of(2018, 5, 1, 17, 59, 59);
    TimeMachine.useFixedClockAt(now);

    // When
    boolean result = tournament.areSubscriptionsOpened();

    // Then
    assertThat(result).isFalse();
  }

  @Test
  public void areSubscriptionsOpened_should_return_true_between_First_of_May_and_10th_of_June() {
    // Given
    LocalDateTime now = LocalDateTime.of(2018, 5, 1, 18, 0, 1);
    TimeMachine.useFixedClockAt(now);

    // When
    boolean result = tournament.areSubscriptionsOpened();

    // Then
    assertThat(result).isTrue();
  }

  @Test
  public void areSubscriptionsOpened_should_return_false_between_First_of_May_and_10th_of_June() {
    // Given
    LocalDateTime now = LocalDateTime.of(2018, 6, 10, 0, 0, 1);
    TimeMachine.useFixedClockAt(now);

    // When
    boolean result = tournament.areSubscriptionsOpened();

    // Then
    assertThat(result).isFalse();
  }
}