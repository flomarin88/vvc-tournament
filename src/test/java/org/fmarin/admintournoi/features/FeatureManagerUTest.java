package org.fmarin.admintournoi.features;

import org.fmarin.admintournoi.helper.TimeMachine;
import org.fmarin.admintournoi.subscription.SubscriptionProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeatureManagerUTest {

  private FeatureManager service;

  @Mock
  private SubscriptionProperties mockedSubscriptionProperties;

  @Before
  public void setUp() {
    service = new FeatureManager(mockedSubscriptionProperties);
  }

  @Test
  public void areSubscriptionsOpened_should_return_false_before_First_of_May_6_PM() {
    // Given
    LocalDateTime now = LocalDateTime.of(2018, 5, 1, 17, 59, 59);
    TimeMachine.useFixedClockAt(now);

    // When
    boolean result = service.areSubscriptionsOpened();

    // Then
    assertThat(result).isFalse();
  }

  @Test
  public void areSubscriptionsOpened_should_return_true_between_First_of_May_and_10th_of_June() {
    // Given
    LocalDateTime now = LocalDateTime.of(2018, 5, 1, 18, 0, 1);
    TimeMachine.useFixedClockAt(now);

    // When
    boolean result = service.areSubscriptionsOpened();

    // Then
    assertThat(result).isTrue();
  }

  @Test
  public void areSubscriptionsOpened_should_return_false_between_First_of_May_and_10th_of_June() {
    // Given
    LocalDateTime now = LocalDateTime.of(2018, 6, 10, 0, 0, 1);
    TimeMachine.useFixedClockAt(now);

    // When
    boolean result = service.areSubscriptionsOpened();

    // Then
    assertThat(result).isFalse();
  }

  @Test
  public void areSubscriptionsEnabled_should_return_true_when_properties_is_enable() {
      // Given
      when(mockedSubscriptionProperties.isEnable()).thenReturn(true);

      // When
      boolean result = service.areSubscriptionsEnabled();

      // Then
      assertThat(result).isTrue();
  }

  @Test
  public void areSubscriptionsEnabled_should_return_false_when_properties_is_disable() {
    // Given
    when(mockedSubscriptionProperties.isEnable()).thenReturn(false);

    // When
    boolean result = service.areSubscriptionsEnabled();

    // Then
    assertThat(result).isFalse();
  }
}