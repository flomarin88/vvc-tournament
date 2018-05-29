package org.fmarin.admintournoi.admin.round;

public final class PreviousRoundBuilder {
  private Long id;
  private Round previousRound;
  private Round round;
  private Integer teamsFrom;
  private Integer teamsTo;

  private PreviousRoundBuilder() {
  }

  public static PreviousRoundBuilder aPreviousRound() {
    return new PreviousRoundBuilder();
  }

  public PreviousRoundBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public PreviousRoundBuilder withPreviousRound(Round previousRound) {
    this.previousRound = previousRound;
    return this;
  }

  public PreviousRoundBuilder withRound(Round round) {
    this.round = round;
    return this;
  }

  public PreviousRoundBuilder withTeamsFrom(Integer teamsFrom) {
    this.teamsFrom = teamsFrom;
    return this;
  }

  public PreviousRoundBuilder withTeamsTo(Integer teamsTo) {
    this.teamsTo = teamsTo;
    return this;
  }

  public PreviousRound build() {
    PreviousRound object = new PreviousRound();
    object.setId(id);
    object.setPreviousRound(previousRound);
    object.setRound(round);
    object.setTeamsFrom(teamsFrom);
    object.setTeamsTo(teamsTo);
    return object;
  }
}
