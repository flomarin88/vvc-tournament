package org.fmarin.admintournoi.admin.team;

import org.fmarin.admintournoi.subscription.Level;

public class TeamOverviewView {

  private final Long id;
  private final String letter;
  private final String name;
  private final Level level;
  private final Integer previousRank;
  private final String previousBranch;
  private final boolean playedAlready;

  public TeamOverviewView(Long id, String letter, String name, Level level, Integer previousRank, String previousBranch, boolean playedAlready) {
    this.id = id;
    this.letter = letter;
    this.name = name;
    this.level = level;
    this.previousRank = previousRank;
    this.previousBranch = previousBranch;
    this.playedAlready = playedAlready;
  }

  public Long getId() {
    return id;
  }

  public String getLetter() {
    return letter;
  }

  public String getName() {
    return name;
  }

  public Integer getPreviousRank() {
    return previousRank;
  }

  public boolean isPlayedAlready() {
    return playedAlready;
  }

  public String getLevelName() {
    return level.getLabel();
  }

  public String getLevelColor() {
    return level.getColor();
  }

  public String getPreviousBranch() {
    return previousBranch;
  }
}
