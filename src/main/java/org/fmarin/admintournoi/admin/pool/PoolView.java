package org.fmarin.admintournoi.admin.pool;

import org.fmarin.admintournoi.admin.team.TeamOverviewView;

import java.util.List;

public class PoolView {

  private final Long id;
  private final String name;
  private final Integer field;
  private final boolean finished;
  private final List<TeamOverviewView> teams;

  public PoolView(Long id, String name, Integer field, boolean finished, List<TeamOverviewView> teams) {
    this.id = id;
    this.name = name;
    this.field = field;
    this.finished = finished;
    this.teams = teams;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Integer getField() {
    return field;
  }

  public boolean isFinished() {
    return finished;
  }

  public List<TeamOverviewView> getTeams() {
    return teams;
  }
}
