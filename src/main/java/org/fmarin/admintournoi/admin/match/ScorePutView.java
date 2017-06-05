package org.fmarin.admintournoi.admin.match;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ScorePutView {

    @NotNull
    @Min(value = 0)
    private Integer teamScore1;
    @NotNull
    @Min(value = 0)
    private Integer teamScore2;

    public ScorePutView(Integer teamScore1, Integer teamScore2) {
        this.teamScore1 = teamScore1;
        this.teamScore2 = teamScore2;
    }

    public ScorePutView() {
    }

    public void setTeamScore1(Integer teamScore1) {
        this.teamScore1 = teamScore1;
    }

    public void setTeamScore2(Integer teamScore2) {
        this.teamScore2 = teamScore2;
    }

    public Integer getTeamScore1() {
        return teamScore1;
    }

    public Integer getTeamScore2() {
        return teamScore2;
    }
}
