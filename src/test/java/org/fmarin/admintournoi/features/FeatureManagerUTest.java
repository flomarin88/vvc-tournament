package org.fmarin.admintournoi.features;

import org.fmarin.admintournoi.subscription.SubscriptionProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeatureManagerUTest {

  private FeatureManager service;

  @Mock
  private SubscriptionProperties mockedSubscriptionProperties;
  @Mock
  private FeatureRepository mockedFeatureRepository;

  @Before
  public void setUp() {
    service = new FeatureManager(mockedSubscriptionProperties, mockedFeatureRepository);
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