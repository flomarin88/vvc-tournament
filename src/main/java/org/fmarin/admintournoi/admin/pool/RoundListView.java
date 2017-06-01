package org.fmarin.admintournoi.admin.pool;

public class RoundListView {

    private Long id;
    private String name;
    private String type;
    private String previousRoundName;
    private String tournamentName;
    private int teamsCount;
    private String status;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPreviousRoundName() {
        return previousRoundName;
    }

    public void setPreviousRoundName(String previousRoundName) {
        this.previousRoundName = previousRoundName;
    }

    public int getTeamsCount() {
        return teamsCount;
    }

    public void setTeamsCount(int teamsCount) {
        this.teamsCount = teamsCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }
}
