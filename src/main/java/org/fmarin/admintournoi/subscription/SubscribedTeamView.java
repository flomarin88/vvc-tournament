package org.fmarin.admintournoi.subscription;

import java.util.List;

public class SubscribedTeamView {

  private final Integer index;
  private final String name;
  private final List<String> players;

  public SubscribedTeamView(Integer index, String name, List<String> players) {
    this.index = index;
    this.name = name;
    this.players = players;
  }

  public Integer getIndex() {
    return index;
  }

  public String getName() {
    return name;
  }

  public List<String> getPlayers() {
    return players;
  }
}
