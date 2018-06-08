package org.fmarin.admintournoi.admin.round;

public enum RoundStatus {

  CREATED("Créé"),
  COMPOSED("À valider"),
  STARTED("En cours"),
  FINISHED("Terminé");

  private String label;

  RoundStatus(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
