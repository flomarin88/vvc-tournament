package org.fmarin.admintournoi.admin.match;

public class MatchPaper {

    private final String roundName;
    private final Integer pool;
    private final Integer field;
    private final String teamA;
    private final String teamB;
    private final String teamC;

    public MatchPaper(String roundName, Integer pool, Integer field, String teamA, String teamB, String teamC) {
        this.roundName = roundName;
        this.pool = pool;
        this.field = field;
        this.teamA = teamA;
        this.teamB = teamB;
        this.teamC = teamC;
    }

    public String getRoundName() {
        return roundName;
    }

    public Integer getPool() {
        return pool;
    }

    public Integer getField() {
        return field;
    }

    public String getTeamA() {
        return teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public String getTeamC() {
        return teamC;
    }
}
