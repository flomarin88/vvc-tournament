package org.fmarin.admintournoi.admin.round;

public enum TournamentBranch {

  PRINCIPALE("Principal", 0, "primary"),
  SUPER_CONSOLANTE("Super Consolante", 1, "info"),
  CONSOLANTE("Consolante", 2, "default");

  private String label;
  private Integer order;
  private String color;

  TournamentBranch(String label, Integer order, String color) {
    this.label = label;
    this.order = order;
    this.color = color;
  }

  public String getLabel() {
    return label;
  }

  public Integer getOrder() {
    return order;
  }

  public String getColor() {
    return color;
  }
}
