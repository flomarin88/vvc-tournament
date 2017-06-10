package org.fmarin.admintournoi.admin.pool;

public final class PoolDetailViewBuilder {
    private Long id;
    private Integer field;
    private String name;
    private Long tournamentId;
    private String tournamentName;
    private Long roundId;
    private String roundName;
    private String color;
    private String status;

    private PoolDetailViewBuilder() {
    }

    public static PoolDetailViewBuilder aPoolDetailView() {
        return new PoolDetailViewBuilder();
    }

    public PoolDetailViewBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PoolDetailViewBuilder withField(Integer field) {
        this.field = field;
        return this;
    }

    public PoolDetailViewBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PoolDetailViewBuilder withTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
        return this;
    }

    public PoolDetailViewBuilder withTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
        return this;
    }

    public PoolDetailViewBuilder withRoundId(Long roundId) {
        this.roundId = roundId;
        return this;
    }

    public PoolDetailViewBuilder withRoundName(String roundName) {
        this.roundName = roundName;
        return this;
    }

    public PoolDetailViewBuilder withColor(String color) {
        this.color = color;
        return this;
    }

    public PoolDetailViewBuilder withStatus(String status) {
        this.status = status;
        return this;
    }


    public PoolDetailView build() {
        return new PoolDetailView(id, field, name, tournamentId, tournamentName, roundId, roundName, color, status);
    }
}
