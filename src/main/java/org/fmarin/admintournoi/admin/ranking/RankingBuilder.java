package org.fmarin.admintournoi.admin.ranking;

import org.fmarin.admintournoi.subscription.Team;

public final class RankingBuilder {
    private Integer victories;
    private Integer defeats;
    private Integer difference;
    private Integer pointsFor;
    private Integer pointsAgainst;
    private Team team;

    private RankingBuilder() {
    }

    public static RankingBuilder aRanking() {
        return new RankingBuilder();
    }

    public RankingBuilder withVictories(Integer victories) {
        this.victories = victories;
        return this;
    }

    public RankingBuilder withDefeats(Integer defeats) {
        this.defeats = defeats;
        return this;
    }

    public RankingBuilder withDifference(Integer difference) {
        this.difference = difference;
        return this;
    }

    public RankingBuilder withPointsFor(Integer pointsFor) {
        this.pointsFor = pointsFor;
        return this;
    }

    public RankingBuilder withPointsAgainst(Integer pointsAgainst) {
        this.pointsAgainst = pointsAgainst;
        return this;
    }

    public RankingBuilder withTeam(Team team) {
        this.team = team;
        return this;
    }

    public Ranking build() {
        Ranking ranking = new Ranking(team);
        ranking.setVictories(victories);
        ranking.setDefeats(defeats);
        ranking.setDifference(difference);
        ranking.setPointsFor(pointsFor);
        ranking.setPointsAgainst(pointsAgainst);
        return ranking;
    }
}
