package org.fmarin.admintournoi.admin.round;

public enum TournamentBranch {

    PRINCIPALE("Principal"),
    SUPER_CONSOLANTE("Super Consolante"),
    CONSOLANTE("Consolante");

    private String label;

    TournamentBranch(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
