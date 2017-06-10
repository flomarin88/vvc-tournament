package org.fmarin.admintournoi.admin.pool;

public final class PoolViewBuilder {
    private Long id;
    private String name;
    private Integer field;
    private Long teamId1;
    private String teamName1;
    private String teamLevel1;
    private Long teamId2;
    private String teamName2;
    private String teamLevel2;
    private Long teamId3;
    private String teamName3;
    private String teamLevel3;
    private String teamLevelColor1;
    private String teamLevelColor2;
    private String teamLevelColor3;
    private Integer teamPreviousRank1;
    private Integer teamPreviousRank2;
    private Integer teamPreviousRank3;
    private String color;

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

    public PoolViewBuilder withTeamLevel1(String teamLevel1) {
        this.teamLevel1 = teamLevel1;
        return this;
    }

    public PoolViewBuilder withTeamLevel2(String teamLevel2) {
        this.teamLevel2 = teamLevel2;
        return this;
    }

    public PoolViewBuilder withTeamLevel3(String teamLevel3) {
        this.teamLevel3 = teamLevel3;
        return this;
    }

    public PoolViewBuilder withTeamLevelColor1(String teamLevelColor1) {
        this.teamLevelColor1 = teamLevelColor1;
        return this;
    }

    public PoolViewBuilder withTeamLevelColor2(String teamLevelColor2) {
        this.teamLevelColor2 = teamLevelColor2;
        return this;
    }

    public PoolViewBuilder withTeamLevelColor3(String teamLevelColor3) {
        this.teamLevelColor3 = teamLevelColor3;
        return this;
    }

    public PoolViewBuilder withTeamPreviousRank1(Integer teamPreviousRank1) {
        this.teamPreviousRank1 = teamPreviousRank1;
        return this;
    }

    public PoolViewBuilder withTeamPreviousRank2(Integer teamPreviousRank2) {
        this.teamPreviousRank2 = teamPreviousRank2;
        return this;
    }

    public PoolViewBuilder withTeamPreviousRank3(Integer teamPreviousRank3) {
        this.teamPreviousRank3 = teamPreviousRank3;
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
        poolView.setTeamLevel1(teamLevel1);
        poolView.setTeamLevel2(teamLevel2);
        poolView.setTeamLevel3(teamLevel3);
        poolView.setTeamLevelColor1(teamLevelColor1);
        poolView.setTeamLevelColor2(teamLevelColor2);
        poolView.setTeamLevelColor3(teamLevelColor3);
        poolView.setTeamPreviousRank1(teamPreviousRank1);
        poolView.setTeamPreviousRank2(teamPreviousRank2);
        poolView.setTeamPreviousRank3(teamPreviousRank3);
        poolView.setColor(color);
        poolView.setField(field);
        return poolView;
    }

}
