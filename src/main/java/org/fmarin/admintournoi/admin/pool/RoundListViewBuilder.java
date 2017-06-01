package org.fmarin.admintournoi.admin.pool;

public final class RoundListViewBuilder {
    private Long id;
    private String name;
    private String type;
    private String previousRoundName;
    private String tournamentName;
    private int teamsCount;
    private String status;

    private RoundListViewBuilder() {
    }

    public static RoundListViewBuilder aRoundListView() {
        return new RoundListViewBuilder();
    }

    public RoundListViewBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public RoundListViewBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public RoundListViewBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public RoundListViewBuilder withPreviousRoundName(String previousRoundName) {
        this.previousRoundName = previousRoundName;
        return this;
    }

    public RoundListViewBuilder withTeamsCount(int teamsCount) {
        this.teamsCount = teamsCount;
        return this;
    }

    public RoundListViewBuilder withStatus(String status) {
        this.status = status;
        return this;
    }

    public RoundListViewBuilder withTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
        return this;
    }

    public RoundListView build() {
        RoundListView roundListView = new RoundListView();
        roundListView.setId(id);
        roundListView.setName(name);
        roundListView.setType(type);
        roundListView.setTournamentName(tournamentName);
        roundListView.setPreviousRoundName(previousRoundName);
        roundListView.setTeamsCount(teamsCount);
        roundListView.setStatus(status);
        return roundListView;
    }
}
