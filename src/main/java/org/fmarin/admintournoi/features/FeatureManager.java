package org.fmarin.admintournoi.features;

import org.fmarin.admintournoi.subscription.SubscriptionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeatureManager {

  private final String CHECKIN_FEATURE = "checkin";

  private final SubscriptionProperties subscriptionProperties;
  private final FeatureRepository featureRepository;

  @Autowired
  public FeatureManager(SubscriptionProperties subscriptionProperties, FeatureRepository featureRepository) {
    this.subscriptionProperties = subscriptionProperties;
    this.featureRepository = featureRepository;
  }

  public boolean areSubscriptionsEnabled() {
    return subscriptionProperties.isEnable();
  }

  public boolean isCheckinEnabled() {
    return featureRepository.findByName(CHECKIN_FEATURE).getEnabled();
  }
}
