package org.fmarin.admintournoi.admin.pool;

public enum RoundStatus {

    CREATED("Créé"),
    COMPOSING("Composition en cours"),
    VALIDATED("Composition validée"),
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
