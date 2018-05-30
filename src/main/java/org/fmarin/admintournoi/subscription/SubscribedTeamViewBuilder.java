package org.fmarin.admintournoi.subscription;

public final class SubscribedTeamViewBuilder {
  private Integer index;
  private String name;

  private SubscribedTeamViewBuilder() {
  }

  public static SubscribedTeamViewBuilder aTeam() {
    return new SubscribedTeamViewBuilder();
  }

  public SubscribedTeamViewBuilder withIndex(Integer index) {
    this.index = index;
    return this;
  }

  public SubscribedTeamViewBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public SubscribedTeamView build() {
    return new SubscribedTeamView(index, name);
  }
}
