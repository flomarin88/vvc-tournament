package org.fmarin.admintournoi.admin.round;

import javax.validation.constraints.NotNull;

public class SwitchTeamsFromPool {

    @NotNull
    private Long team1Id;
    @NotNull
    private Long pool1Id;
    @NotNull
    private Long team2Id;
    @NotNull
    private Long pool2Id;

    public Long getTeam1Id() {
        return team1Id;
    }

    public void setTeam1Id(Long team1Id) {
        this.team1Id = team1Id;
    }

    public Long getPool1Id() {
        return pool1Id;
    }

    public void setPool1Id(Long pool1Id) {
        this.pool1Id = pool1Id;
    }

    public Long getTeam2Id() {
        return team2Id;
    }

    public void setTeam2Id(Long team2Id) {
        this.team2Id = team2Id;
    }

    public Long getPool2Id() {
        return pool2Id;
    }

    public void setPool2Id(Long pool2Id) {
        this.pool2Id = pool2Id;
    }
}
