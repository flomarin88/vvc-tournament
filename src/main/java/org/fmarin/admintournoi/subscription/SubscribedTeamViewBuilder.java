package org.fmarin.admintournoi.subscription;

import java.util.List;

public final class SubscribedTeamViewBuilder {
  private Integer index;
  private String name;
  private List<String> players;

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

  public SubscribedTeamViewBuilder withPlayers(List<String> players) {
    this.players = players;
    return this;
  }

  public SubscribedTeamView build() {
    return new SubscribedTeamView(index, name, players);
  }
}
