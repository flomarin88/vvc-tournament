package org.fmarin.admintournoi.admin.pool;

import org.fmarin.admintournoi.admin.match.Match;
import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.subscription.Team;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "field")
    private Integer field;

    @OneToMany(mappedBy = "pool", cascade = CascadeType.ALL)
    private List<Match> matches = new ArrayList<>();

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

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public Integer getField() {
        return field;
    }

    public void setField(Integer field) {
        this.field = field;
    }
}
