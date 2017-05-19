package org.fmarin.admintournoi.admin.pool;

public final class RoundViewBuilder {
    private Long id;
    private String name;
    private String previousRoundName;
    private int teamsCount;
    private String status;

    private RoundViewBuilder() {
    }

    public static RoundViewBuilder aPoolView() {
        return new RoundViewBuilder();
    }

    public RoundViewBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public RoundViewBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public RoundViewBuilder withPreviousRoundName(String previousRoundName) {
        this.previousRoundName = previousRoundName;
        return this;
    }

    public RoundViewBuilder withTeamsCount(int teamsCount) {
        this.teamsCount = teamsCount;
        return this;
    }

    public RoundViewBuilder withStatus(String status) {
        this.status = status;
        return this;
    }

    public RoundView build() {
        RoundView roundView = new RoundView();
        roundView.setId(id);
        roundView.setName(name);
        roundView.setPreviousRoundName(previousRoundName);
        roundView.setTeamsCount(teamsCount);
        roundView.setStatus(status);
        return roundView;
    }
}
