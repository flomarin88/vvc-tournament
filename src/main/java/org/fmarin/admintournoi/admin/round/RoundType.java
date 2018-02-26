package org.fmarin.admintournoi.admin.round;

public enum RoundType {

    POOL("Poules"),
    DIRECT_ELIMINATION("Élimination directe");

    private String label;

    RoundType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}