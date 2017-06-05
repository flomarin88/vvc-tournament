package org.fmarin.admintournoi.admin.pool;

public class PoolDetailView {

    private final Long id;
    private final String name;
    private final Long tournamentId;
    private final String tournamentName;
    private final Long roundId;
    private final String roundName;

    public PoolDetailView(Long id, String name, Long tournamentId, String tournamentName, Long roundId, String roundName) {
        this.id = id;
        this.name = name;
        this.tournamentId = tournamentId;
        this.tournamentName = tournamentName;
        this.roundId = roundId;
        this.roundName = roundName;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public Long getRoundId() {
        return roundId;
    }

    public String getRoundName() {
        return roundName;
    }
}
