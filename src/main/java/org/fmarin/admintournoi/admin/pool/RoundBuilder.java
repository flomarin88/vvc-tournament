package org.fmarin.admintournoi.admin.pool;

import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.Tournament;

import java.util.List;

public final class RoundBuilder {
    private Long id;
    private String name;
    private Round previousRound;
    private Tournament tournament;
    private TournamentBranch branch;
    private RoundType type;
    private List<Team> teams;
    private RoundStatus status;

    private RoundBuilder() {
    }

    public static RoundBuilder aRound() {
        return new RoundBuilder();
    }

    public RoundBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public RoundBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public RoundBuilder withType(RoundType type) {
        this.type = type;
        return this;
    }

    public RoundBuilder withPreviousRound(Round previousRound) {
        this.previousRound = previousRound;
        return this;
    }

    public RoundBuilder withTournament(Tournament tournament) {
        this.tournament = tournament;
        return this;
    }

    public RoundBuilder withBranch(TournamentBranch branch) {
        this.branch = branch;
        return this;
    }

    public RoundBuilder withTeams(List<Team> teams) {
        this.teams = teams;
        return this;
    }

    public RoundBuilder withStatus(RoundStatus status) {
        this.status = status;
        return this;
    }

    public Round build() {
        Round round = new Round();
        round.setId(id);
        round.setName(name);
        round.setType(type);
        round.setPreviousRound(previousRound);
        round.setTournament(tournament);
        round.setBranch(branch);
        round.setTeams(teams);
        round.setStatus(status);
        return round;
    }
}
