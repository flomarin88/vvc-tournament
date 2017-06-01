package org.fmarin.admintournoi.admin.pool;

public final class PoolViewBuilder {
    private Long id;
    private String name;
    private Long teamId1;
    private String teamName1;
    private Long teamId2;
    private String teamName2;
    private Long teamId3;
    private String teamName3;

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

    public PoolViewBuilder withTeamId1(Long teamId1) {
        this.teamId1 = teamId1;
        return this;
    }

    public PoolViewBuilder withTeamName1(String teamName1) {
        this.teamName1 = teamName1;
        return this;
    }

    public PoolViewBuilder withTeamId2(Long teamId2) {
        this.teamId2 = teamId2;
        return this;
    }

    public PoolViewBuilder withTeamName2(String teamName2) {
        this.teamName2 = teamName2;
        return this;
    }

    public PoolViewBuilder withTeamId3(Long teamId3) {
        this.teamId3 = teamId3;
        return this;
    }

    public PoolViewBuilder withTeamName3(String teamName3) {
        this.teamName3 = teamName3;
        return this;
    }

    public PoolView build() {
        PoolView poolView = new PoolView();
        poolView.setId(id);
        poolView.setName(name);
        poolView.setTeamId1(teamId1);
        poolView.setTeamName1(teamName1);
        poolView.setTeamId2(teamId2);
        poolView.setTeamName2(teamName2);
        poolView.setTeamId3(teamId3);
        poolView.setTeamName3(teamName3);
        return poolView;
    }
}
