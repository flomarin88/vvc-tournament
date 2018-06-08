package org.fmarin.admintournoi.admin.round;

public enum RoundType {

  POOL("Poules", 3),
  DIRECT_ELIMINATION("Élim.", 2);

  private String label;
  private Integer teamsCount;

  RoundType(String label, Integer teamsCount) {
    this.label = label;
    this.teamsCount = teamsCount;
  }

  public String getLabel() {
    return label;
  }

  public Integer getTeamsCount() {
    return teamsCount;
  }
}
