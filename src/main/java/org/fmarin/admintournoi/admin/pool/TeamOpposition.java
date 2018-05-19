package org.fmarin.admintournoi.admin.pool;

import org.fmarin.admintournoi.subscription.Team;

import java.util.Objects;

public class TeamOpposition {

  private final Team team1;
  private final Team team2;

  public TeamOpposition(Team team1, Team team2) {
    this.team1 = team1;
    this.team2 = team2;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TeamOpposition that = (TeamOpposition) o;
    return (Objects.equals(team1, that.team1) &&
      Objects.equals(team2, that.team2)) || (Objects.equals(team2, that.team1) &&
      Objects.equals(team1, that.team2));
  }

  @Override
  public int hashCode() {
    return Objects.hash(team1, team2);
  }
}
