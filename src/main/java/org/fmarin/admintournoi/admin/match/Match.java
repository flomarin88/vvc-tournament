package org.fmarin.admintournoi.admin.match;

import org.fmarin.admintournoi.admin.pool.Pool;
import org.fmarin.admintournoi.subscription.Team;

import javax.persistence.*;

@Entity
@Table(name = "MATCH")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pool_id")
    private Pool pool;

    @ManyToOne
    @JoinColumn(name = "team1_id")
    private Team team1;
    @ManyToOne
    @JoinColumn(name = "team2_id")
    private Team team2;

    @Column(name = "score_team1")
    private Integer scoreTeam1 = 0;
    @Column(name = "score_team2")
    private Integer scoreTeam2 = 0;

    public boolean isFinished() {
        return scoreTeam1 != 0 || scoreTeam2 !=0;
    }

    public Integer getPointsFor(Team team) {
        if (team.equals(team1)) {
            return scoreTeam1;
        }
        if (team.equals(team2)) {
            return scoreTeam2;
        }
        return 0;
    }

    public Integer getPointsAgainst(Team team) {
        if (team.equals(team1)) {
            return scoreTeam2;
        }
        if (team.equals(team2)) {
            return scoreTeam1;
        }
        return 0;
    }

    public MatchStatus getStatus(Team team) {
        if ((!team.equals(team1) && !team.equals(team2)) || (scoreTeam1.equals(scoreTeam2))) {
            return MatchStatus.DRAW;
        }
        if (scoreTeam1 > scoreTeam2) {
            return team1.equals(team) ? MatchStatus.VICTORY : MatchStatus.DEFEAT;
        }
        else {
            return team1.equals(team) ? MatchStatus.DEFEAT : MatchStatus.VICTORY;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public Integer getScoreTeam1() {
        return scoreTeam1;
    }

    public void setScoreTeam1(Integer scoreTeam1) {
        this.scoreTeam1 = scoreTeam1;
    }

    public Integer getScoreTeam2() {
        return scoreTeam2;
    }

    public void setScoreTeam2(Integer scoreTeam2) {
        this.scoreTeam2 = scoreTeam2;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }
}
