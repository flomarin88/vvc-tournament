package org.fmarin.admintournoi.admin.pool;

import org.fmarin.admintournoi.admin.team.TeamOverviewView;

import java.util.List;

public final class PoolViewBuilder {
    private Long id;
    private String name;
    private Integer field;
    private String color;
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

    public PoolViewBuilder withColor(String color) {
        this.color = color;
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
        PoolView poolView = new PoolView();
        poolView.setId(id);
        poolView.setName(name);
        poolView.setTeams(teams);
        poolView.setColor(color);
        poolView.setField(field);
        return poolView;
    }

}
