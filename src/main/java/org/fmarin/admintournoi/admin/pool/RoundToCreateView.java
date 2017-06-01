package org.fmarin.admintournoi.admin.pool;

import java.util.List;

public class RoundToCreateView {

    private String name;
    private String tournamentBranch;
    private String type;
    private Long previousRoundId;
    private List<Long> teamsId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTournamentBranch() {
        return tournamentBranch;
    }

    public void setTournamentBranch(String tournamentBranch) {
        this.tournamentBranch = tournamentBranch;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getPreviousRoundId() {
        return previousRoundId;
    }

    public void setPreviousRoundId(Long previousRoundId) {
        this.previousRoundId = previousRoundId;
    }

    public List<Long> getTeamsId() {
        return teamsId;
    }

    public void setTeamsId(List<Long> teamsId) {
        this.teamsId = teamsId;
    }
}
