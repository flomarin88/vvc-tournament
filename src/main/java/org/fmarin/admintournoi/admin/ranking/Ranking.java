package org.fmarin.admintournoi.admin.ranking;

import com.google.common.base.Objects;
import org.fmarin.admintournoi.subscription.Team;

public class Ranking {

    private Integer position;
    private final Team team;
    private Integer victories = 0;
    private Integer defeats = 0;
    private Integer difference = 0;
    private Integer pointsFor = 0;
    private Integer pointsAgainst = 0;

    public Ranking(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public Integer getVictories() {
        return victories;
    }

    public Integer getDefeats() {
        return defeats;
    }

    public Integer getPointsFor() {
        return pointsFor;
    }

    public Integer getPointsAgainst() {
        return pointsAgainst;
    }

    public Integer getDifference() {
        return difference;
    }

    public void setVictories(Integer victories) {
        this.victories = victories;
    }

    public void setDefeats(Integer defeats) {
        this.defeats = defeats;
    }

    public void setDifference(Integer difference) {
        this.difference = difference;
    }

    public void setPointsFor(Integer pointsFor) {
        this.pointsFor = pointsFor;
    }

    public void setPointsAgainst(Integer pointsAgainst) {
        this.pointsAgainst = pointsAgainst;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ranking ranking = (Ranking) o;
        return Objects.equal(team, ranking.team) &&
                Objects.equal(position, ranking.position) &&
                Objects.equal(victories, ranking.victories) &&
                Objects.equal(defeats, ranking.defeats) &&
                Objects.equal(difference, ranking.difference) &&
                Objects.equal(pointsFor, ranking.pointsFor) &&
                Objects.equal(pointsAgainst, ranking.pointsAgainst);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(team, victories, defeats, difference, pointsFor, pointsAgainst);
    }

    @Override
    public String toString() {
        return "Ranking{" +
                "team=" + team +
                ", position=" + position +
                ", victories=" + victories +
                ", defeats=" + defeats +
                ", difference=" + difference +
                ", pointsFor=" + pointsFor +
                ", pointsAgainst=" + pointsAgainst +
                '}';
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
