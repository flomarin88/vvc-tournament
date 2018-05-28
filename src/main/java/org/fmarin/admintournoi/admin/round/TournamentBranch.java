package org.fmarin.admintournoi.admin.round;

public enum TournamentBranch {

    PRINCIPALE("Principal", 0),
    SUPER_CONSOLANTE("Super Consolante", 1),
    CONSOLANTE("Consolante", 2);

    private String label;
    private Integer order;

    TournamentBranch(String label, Integer order) {
        this.label = label;
        this.order = order;
    }

    public String getLabel() {
        return label;
    }

  public Integer getOrder() {
    return order;
  }
}
