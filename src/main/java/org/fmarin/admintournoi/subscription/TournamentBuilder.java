package org.fmarin.admintournoi.subscription;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class TournamentBuilder {
  private Long id;
  private String name;
  private int teamLimit;
  private List<Team> teams = new ArrayList<>();
  private String paypalButtonId;
  private LocalDateTime subscriptionsOpeningDate;
  private LocalDateTime subscriptionsClosingDate;
  private Gender gender;

  private TournamentBuilder() {
  }

  public static TournamentBuilder aTournament() {
    return new TournamentBuilder();
  }

  public TournamentBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public TournamentBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public TournamentBuilder withTeamLimit(int teamLimit) {
    this.teamLimit = teamLimit;
    return this;
  }

  public TournamentBuilder withTeams(List<Team> teams) {
    this.teams = teams;
    return this;
  }

  public TournamentBuilder withPaypalButtonId(String paypalButtonId) {
    this.paypalButtonId = paypalButtonId;
    return this;
  }

  public TournamentBuilder subscriptionsOpenAt(LocalDateTime opensAt) {
    this.subscriptionsOpeningDate = opensAt;
    return this;
  }

  public TournamentBuilder subscriptionsCloseAt(LocalDateTime closesAt) {
    this.subscriptionsClosingDate = closesAt;
    return this;
  }

  public TournamentBuilder withGender(Gender gender) {
    this.gender = gender;
    return this;
  }

  public Tournament build() {
    Tournament tournament = new Tournament();
    tournament.setId(id);
    tournament.setName(name);
    tournament.setTeamLimit(teamLimit);
    tournament.setTeams(teams);
    tournament.setGender(gender);
    tournament.setPaypalButtonId(paypalButtonId);
    tournament.setSubscriptionsOpeningDate(subscriptionsOpeningDate);
    tournament.setSubscriptionsClosingDate(subscriptionsClosingDate);
    return tournament;
  }
}
