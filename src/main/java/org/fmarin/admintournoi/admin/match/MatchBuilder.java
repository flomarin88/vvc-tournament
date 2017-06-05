package org.fmarin.admintournoi.admin.match;

import org.fmarin.admintournoi.admin.pool.Pool;
import org.fmarin.admintournoi.subscription.Team;

public final class MatchBuilder {
    private Long id;
    private Pool pool;
    private Team team1;
    private Team team2;
    private Integer scoreTeam1;
    private Integer scoreTeam2;

    private MatchBuilder() {
    }

    public static MatchBuilder aMatch() {
        return new MatchBuilder();
    }

    public MatchBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public MatchBuilder withPool(Pool pool) {
        this.pool = pool;
        return this;
    }

    public MatchBuilder withTeam1(Team team1) {
        this.team1 = team1;
        return this;
    }

    public MatchBuilder withTeam2(Team team2) {
        this.team2 = team2;
        return this;
    }

    public MatchBuilder withScoreTeam1(Integer scoreTeam1) {
        this.scoreTeam1 = scoreTeam1;
        return this;
    }

    public MatchBuilder withScoreTeam2(Integer scoreTeam2) {
        this.scoreTeam2 = scoreTeam2;
        return this;
    }

    public Match build() {
        Match match = new Match();
        match.setId(id);
        match.setPool(pool);
        match.setTeam1(team1);
        match.setTeam2(team2);
        match.setScoreTeam1(scoreTeam1);
        match.setScoreTeam2(scoreTeam2);
        return match;
    }
}
