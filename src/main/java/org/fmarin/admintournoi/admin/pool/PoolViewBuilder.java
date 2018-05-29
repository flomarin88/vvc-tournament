package org.fmarin.admintournoi.admin.pool;

import org.fmarin.admintournoi.admin.team.TeamOverviewView;

import java.util.List;

public final class PoolViewBuilder {
    private Long id;
    private String name;
    private Integer field;
    private boolean finished;
    private List<TeamOverviewView> teams;

    private PoolViewBuilder() {
    }

    public static PoolViewBuilder aPoolView() {
        return new PoolViewBuilder();
    }

    public PoolViewBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PoolViewBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PoolViewBuilder isFinished() {
        this.finished = true;
        return this;
    }

    public PoolViewBuilder withField(Integer field) {
        this.field = field;
        return this;
    }

    public PoolViewBuilder withTeams(List<TeamOverviewView> teams) {
        this.teams = teams;
      return this;
    }

    public PoolView build() {
        return new PoolView(id, name, field, finished, teams);
    }
}
