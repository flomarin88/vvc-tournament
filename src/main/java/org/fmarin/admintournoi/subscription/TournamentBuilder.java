package org.fmarin.admintournoi.subscription;

import java.util.ArrayList;
import java.util.List;

public final class TournamentBuilder {
    private Long id;
    private String name;
    private int teamLimit;
    private List<Team> teams = new ArrayList<>();
    private String paypalButtonId;

    private TournamentBuilder() {
    }

    public static TournamentBuilder aTournament() {
        return new TournamentBuilder();
    }

    public TournamentBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TournamentBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TournamentBuilder withTeamLimit(int teamLimit) {
        this.teamLimit = teamLimit;
        return this;
    }

    public TournamentBuilder withTeams(List<Team> teams) {
        this.teams = teams;
        return this;
    }

    public TournamentBuilder withPaypalButtonId(String paypalButtonId) {
        this.paypalButtonId = paypalButtonId;
        return this;
    }

    public Tournament build() {
        Tournament tournament = new Tournament();
        tournament.setId(id);
        tournament.setName(name);
        tournament.setTeamLimit(teamLimit);
        tournament.setTeams(teams);
        tournament.setPaypalButtonId(paypalButtonId);
        return tournament;
    }
}
