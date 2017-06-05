package org.fmarin.admintournoi.admin.match;

public class MatchView {

    private final Long id;
    private final String teamName1;
    private final String teamName2;
    private final Integer teamScore1;
    private final Integer teamScore2;

    public MatchView(Long id, String teamName1, String teamName2, Integer teamScore1, Integer teamScore2) {
        this.id = id;
        this.teamName1 = teamName1;
        this.teamName2 = teamName2;
        this.teamScore1 = teamScore1;
        this.teamScore2 = teamScore2;
    }

    public Long getId() {
        return id;
    }

    public String getTeamName1() {
        return teamName1;
    }

    public String getTeamName2() {
        return teamName2;
    }

    public Integer getTeamScore1() {
        return teamScore1;
    }

    public Integer getTeamScore2() {
        return teamScore2;
    }
}
