package org.fmarin.admintournoi.features;

public final class FeatureBuilder {
  private Long id;
  private String name;
  private Boolean enabled = false;

  private FeatureBuilder() {
  }

  public static FeatureBuilder aFeature() {
    return new FeatureBuilder();
  }

  public FeatureBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public FeatureBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public FeatureBuilder isEnabled() {
    this.enabled = true;
    return this;
  }

  public Feature build() {
    Feature feature = new Feature();
    feature.setId(id);
    feature.setName(name);
    feature.setEnabled(enabled);
    return feature;
  }
}
