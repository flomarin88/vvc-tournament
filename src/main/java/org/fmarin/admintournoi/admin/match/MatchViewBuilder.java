package org.fmarin.admintournoi.admin.match;

public final class MatchViewBuilder {
    private Long id;
    private String teamName1;
    private String teamName2;
    private Integer teamScore1;
    private Integer teamScore2;

    private MatchViewBuilder() {
    }

    public static MatchViewBuilder aMatchView() {
        return new MatchViewBuilder();
    }

    public MatchViewBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public MatchViewBuilder withTeamName1(String teamName1) {
        this.teamName1 = teamName1;
        return this;
    }

    public MatchViewBuilder withTeamName2(String teamName2) {
        this.teamName2 = teamName2;
        return this;
    }

    public MatchViewBuilder withTeamScore1(Integer teamScore1) {
        this.teamScore1 = teamScore1;
        return this;
    }

    public MatchViewBuilder withTeamScore2(Integer teamScore2) {
        this.teamScore2 = teamScore2;
        return this;
    }

    public MatchView build() {
        return new MatchView(id, teamName1, teamName2, teamScore1, teamScore2);
    }
}
