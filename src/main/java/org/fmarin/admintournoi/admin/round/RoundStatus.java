package org.fmarin.admintournoi.admin.round;

public enum RoundStatus {

    CREATED("Créé"),
    COMPOSED("Composition à valider"),
    STARTED("Matchs en cours"),
    FINISHED("Terminé");

    private String label;

    RoundStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
