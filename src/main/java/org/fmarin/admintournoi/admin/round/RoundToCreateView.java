package org.fmarin.admintournoi.admin.round;

public class RoundToCreateView {

    private String name;
    private String tournamentBranch;
    private String type;
    private Long previousRoundId;
    private Integer teamsFrom;
    private Integer teamsTo;
    private String fieldRanges;

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

    public Integer getTeamsFrom() {
        return teamsFrom;
    }

    public void setTeamsFrom(Integer teamsFrom) {
        this.teamsFrom = teamsFrom;
    }

    public Integer getTeamsTo() {
        return teamsTo;
    }

    public void setTeamsTo(Integer teamsTo) {
        this.teamsTo = teamsTo;
    }

    public String getFieldRanges() {
        return fieldRanges;
    }

    public void setFieldRanges(String fieldRanges) {
        this.fieldRanges = fieldRanges;
    }
}
