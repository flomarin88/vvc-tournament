package org.fmarin.admintournoi.admin.pool;

import org.fmarin.admintournoi.admin.match.Match;
import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.subscription.Team;

import java.util.ArrayList;
import java.util.List;

public final class PoolBuilder {
    private Long id;
    private Round round;
    private Integer position;
    private Team team1;
    private Team team2;
    private Team team3;
    private List<Match> matches = new ArrayList<>();
    private Integer field;

    private PoolBuilder() {
    }

    public static PoolBuilder aPool() {
        return new PoolBuilder();
    }

    public PoolBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PoolBuilder withRound(Round round) {
        this.round = round;
        return this;
    }

    public PoolBuilder withField(Integer field) {
        this.field = field;
        return this;
    }

    public PoolBuilder withPosition(Integer position) {
        this.position = position;
        return this;
    }

    public PoolBuilder withTeam1(Team team1) {
        this.team1 = team1;
        return this;
    }

    public PoolBuilder withTeam2(Team team2) {
        this.team2 = team2;
        return this;
    }

    public PoolBuilder withTeam3(Team team3) {
        this.team3 = team3;
        return this;
    }

    public PoolBuilder withMatches(List<Match> matches) {
        this.matches = matches;
        return this;
    }

    public Pool build() {
        Pool pool = new Pool();
        pool.setId(id);
        pool.setRound(round);
        pool.setPosition(position);
        pool.setTeam1(team1);
        pool.setTeam2(team2);
        pool.setTeam3(team3);
        pool.setMatches(matches);
        pool.setField(field);
        return pool;
    }
}
