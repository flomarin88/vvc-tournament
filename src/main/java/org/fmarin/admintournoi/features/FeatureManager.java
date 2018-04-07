package org.fmarin.admintournoi.features;

import org.fmarin.admintournoi.helper.TimeMachine;
import org.fmarin.admintournoi.subscription.SubscriptionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FeatureManager {

  private final SubscriptionProperties subscriptionProperties;

  @Autowired
  public FeatureManager(SubscriptionProperties subscriptionProperties) {
    this.subscriptionProperties = subscriptionProperties;
  }

  public boolean areSubscriptionsOpened() {
    LocalDateTime opening = LocalDateTime.of(2018, 5, 1, 18, 0, 0);
    LocalDateTime closing = LocalDateTime.of(2018, 6, 10, 0, 0, 0);

    LocalDateTime now = TimeMachine.now();
    return now.isAfter(opening) && now.isBefore(closing);
  }

  public boolean areSubscriptionsEnabled() {
    return subscriptionProperties.isEnable();
  }
}
