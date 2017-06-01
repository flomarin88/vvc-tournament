package org.fmarin.admintournoi.admin.pool;

import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.Tournament;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ROUND")
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "previous_round_id")
    private Round previousRound;
    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;
    @Column(name = "tournament_branch")
    @Enumerated(value = EnumType.STRING)
    private TournamentBranch branch;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "ROUND_TEAM",
            joinColumns = @JoinColumn(name = "round_id", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "team_id", referencedColumnName = "ID"))
    private List<Team> teams;
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private RoundStatus status;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private RoundType type;

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

    public Round getPreviousRound() {
        return previousRound;
    }

    public void setPreviousRound(Round previousRound) {
        this.previousRound = previousRound;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public RoundStatus getStatus() {
        return status;
    }

    public void setStatus(RoundStatus status) {
        this.status = status;
    }

    public TournamentBranch getBranch() {
        return branch;
    }

    public void setBranch(TournamentBranch branch) {
        this.branch = branch;
    }

    public RoundType getType() {
        return type;
    }

    public void setType(RoundType type) {
        this.type = type;
    }

}
