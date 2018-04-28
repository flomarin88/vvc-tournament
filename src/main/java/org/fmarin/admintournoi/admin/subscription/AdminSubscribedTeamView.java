package org.fmarin.admintournoi.admin.subscription;

import org.fmarin.admintournoi.subscription.Level;

import java.util.List;

public class AdminSubscribedTeamView {

  private String name;
  private Level level;
  private List<PlayerView> players;
  private Integer verificationCode;

  public AdminSubscribedTeamView(String name, Level level, List<PlayerView> players, Integer verificationCode) {
    this.name = name;
    this.level = level;
    this.players = players;
    this.verificationCode = verificationCode;
  }

  public String getName() {
    return name;
  }

  public Level getLevel() {
    return level;
  }

  public List<PlayerView> getPlayers() {
    return players;
  }

  public Integer getVerificationCode() {
    return verificationCode;
  }
}
