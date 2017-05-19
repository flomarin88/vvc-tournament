package org.fmarin.admintournoi.admin.pool;

public enum TournamentBranch {

    PRINCIPALE("Principal"),
    CONSOLANTE("Consolante"),
    SUPER_CONSOLANTE("Super Consolante");

    private String label;

    TournamentBranch(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
