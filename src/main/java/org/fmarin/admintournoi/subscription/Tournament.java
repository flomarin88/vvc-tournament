package org.fmarin.admintournoi.subscription;

import org.fmarin.admintournoi.helper.TimeMachine;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tournament {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "name")
  private String name;
  @Column(name = "gender")
  @Enumerated(EnumType.STRING)
  private Gender gender;
  @Column(name = "year")
  private Integer year;
  @Column(name = "team_limit")
  private int teamLimit;
  @OneToMany(mappedBy = "tournament")
  private List<Team> teams = new ArrayList<>();
  @Column(name = "paypal_button_id")
  private String paypalButtonId;
  @Column(name = "subscriptions_opening_date")
  private LocalDateTime subscriptionsOpeningDate;
  @Column(name = "subscriptions_closing_date")
  private LocalDateTime subscriptionsClosingDate;

  public boolean isFull() {
    long subscriptionCount = teams.stream().filter(team -> "Completed".equals(team.getPaymentStatus())).count();
    return subscriptionCount >= teamLimit;
  }

  public boolean areSubscriptionsOpened() {
    LocalDateTime now = TimeMachine.now();
    return now.isAfter(subscriptionsOpeningDate) && now.isBefore(subscriptionsClosingDate);
  }

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

  public int getTeamLimit() {
    return teamLimit;
  }

  public void setTeamLimit(int teamLimit) {
    this.teamLimit = teamLimit;
  }

  public List<Team> getTeams() {
    return teams;
  }

  public void setTeams(List<Team> teams) {
    this.teams = teams;
  }

  public String getPaypalButtonId() {
    return paypalButtonId;
  }

  public void setPaypalButtonId(String paypalButtonId) {
    this.paypalButtonId = paypalButtonId;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public LocalDateTime getSubscriptionsOpeningDate() {
    return subscriptionsOpeningDate;
  }

  public void setSubscriptionsOpeningDate(LocalDateTime subscriptionsOpeningDate) {
    this.subscriptionsOpeningDate = subscriptionsOpeningDate;
  }

  public LocalDateTime getSubscriptionsClosingDate() {
    return subscriptionsClosingDate;
  }

  public void setSubscriptionsClosingDate(LocalDateTime subscriptionsClosingDate) {
    this.subscriptionsClosingDate = subscriptionsClosingDate;
  }
}
