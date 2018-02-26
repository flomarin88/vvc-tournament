package org.fmarin.admintournoi.admin.round;

import org.fmarin.admintournoi.admin.pool.Pool;
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
    private List<Pool> pools;
    private RoundStatus status;
    private String fieldRanges;

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

    public RoundBuilder withPools(List<Pool> pools) {
        this.pools = pools;
        return this;
    }

    public RoundBuilder withStatus(RoundStatus status) {
        this.status = status;
        return this;
    }

    public RoundBuilder withFieldRanges(String fieldRanges) {
        this.fieldRanges = fieldRanges;
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
        round.setPools(pools);
        round.setStatus(status);
        round.setFieldRanges(fieldRanges);
        return round;
    }
}