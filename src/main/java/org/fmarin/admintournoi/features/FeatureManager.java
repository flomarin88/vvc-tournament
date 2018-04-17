package org.fmarin.admintournoi.features;

import org.fmarin.admintournoi.subscription.SubscriptionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeatureManager {

  private final SubscriptionProperties subscriptionProperties;

  @Autowired
  public FeatureManager(SubscriptionProperties subscriptionProperties) {
    this.subscriptionProperties = subscriptionProperties;
  }

  public boolean areSubscriptionsEnabled() {
    return subscriptionProperties.isEnable();
  }
}
