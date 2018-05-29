package org.fmarin.admintournoi.admin.team;

import org.fmarin.admintournoi.subscription.Level;

public final class TeamOverviewViewBuilder {
  private Long id;
  private String letter;
  private String name;
  private Level level;
  private Integer previousRank;
  private String previousRankColor;
  private boolean playedAlready;

  private TeamOverviewViewBuilder() {
  }

  public static TeamOverviewViewBuilder aTeamOverviewView() {
    return new TeamOverviewViewBuilder();
  }

  public TeamOverviewViewBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public TeamOverviewViewBuilder withLetter(String letter) {
    this.letter = letter;
    return this;
  }

  public TeamOverviewViewBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public TeamOverviewViewBuilder withLevel(Level level) {
    this.level = level;
    return this;
  }

  public TeamOverviewViewBuilder withPreviousRank(Integer previousRank) {
    this.previousRank = previousRank;
    return this;
  }

  public TeamOverviewViewBuilder withPreviousRankColor(String color) {
    this.previousRankColor = color;
    return this;
  }


  public TeamOverviewViewBuilder withPlayedAlready(boolean playedAlready) {
    this.playedAlready = playedAlready;
    return this;
  }

  public TeamOverviewView build() {
    return new TeamOverviewView(id, letter, name, level, previousRank, previousRankColor, playedAlready);
  }
}
