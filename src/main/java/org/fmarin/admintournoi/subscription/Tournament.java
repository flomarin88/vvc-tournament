package org.fmarin.admintournoi.subscription;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "team_limit")
    private int teamLimit;
    @OneToMany(mappedBy = "tournament")
    private List<Team> teams = new ArrayList<>();
    @Column(name = "paypal_button_id")
    private String paypalButtonId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTeamLimit() {
        return teamLimit;
    }

    public void setTeamLimit(int teamLimit) {
        this.teamLimit = teamLimit;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public String getPaypalButtonId() {
        return paypalButtonId;
    }

    public void setPaypalButtonId(String paypalButtonId) {
        this.paypalButtonId = paypalButtonId;
    }
}
