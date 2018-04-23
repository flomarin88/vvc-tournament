package org.fmarin.admintournoi.subscription;

import java.util.List;

public class TeamsByTournamentView {

  private final String name;
  private final List<SubscribedTeamView> teams;

  public TeamsByTournamentView(String name, List<SubscribedTeamView> teams) {
    this.name = name;
    this.teams = teams;
  }

  public String getName() {
    return name;
  }

  public List<SubscribedTeamView> getTeams() {
    return teams;
  }
}
