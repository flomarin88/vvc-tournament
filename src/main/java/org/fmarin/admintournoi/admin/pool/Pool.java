package org.fmarin.admintournoi.admin.pool;

import org.fmarin.admintournoi.subscription.Team;

import javax.persistence.*;

@Entity
@Table(name = "POOL")
public class Pool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "round_id")
    private Round round;

    @Column(name = "position")
    private Integer position;
    @ManyToOne
    @JoinColumn(name = "team_1_id")
    private Team team1;
    @ManyToOne
    @JoinColumn(name = "team_2_id")
    private Team team2;
    @ManyToOne
    @JoinColumn(name = "team_3_id")
    private Team team3;

    public void addTeam(Team team) {
        if (team1 == null) {
            team1 = team;
        } else if (team2 == null) {
            team2 = team;
        } else {
            team3 = team;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
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

    public Team getTeam3() {
        return team3;
    }

    public void setTeam3(Team team3) {
        this.team3 = team3;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }
}
