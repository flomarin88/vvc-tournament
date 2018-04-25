package org.fmarin.admintournoi.subscription;

import java.util.List;

public final class TeamsByTournamentViewBuilder {
  private String name;
  private List<SubscribedTeamView> teams;

  private TeamsByTournamentViewBuilder() {
  }

  public static TeamsByTournamentViewBuilder aView() {
    return new TeamsByTournamentViewBuilder();
  }

  public TeamsByTournamentViewBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public TeamsByTournamentViewBuilder withTeams(List<SubscribedTeamView> teams) {
    this.teams = teams;
    return this;
  }

  public TeamsByTournamentView build() {
    return new TeamsByTournamentView(name, teams);
  }
}
