package org.fmarin.admintournoi.admin.pool;

import org.fmarin.admintournoi.admin.team.TeamOverviewView;

import java.util.List;

public class PoolView {

    private Long id;
    private String name;
    private Integer field;
    private String color;
    private List<TeamOverviewView> teams;

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getField() {
        return field;
    }

    public void setField(Integer field) {
        this.field = field;
    }

    public List<TeamOverviewView> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamOverviewView> teams) {
        this.teams = teams;
    }
}
