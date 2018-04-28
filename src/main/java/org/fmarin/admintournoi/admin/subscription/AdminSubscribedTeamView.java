package org.fmarin.admintournoi.admin.subscription;

import java.util.List;

public class AdminSubscribedTeamView {

  private String name;
  private String level;
  private List<PlayerView> players;
  private Integer verificationCode;

  public AdminSubscribedTeamView(String name, String level, List<PlayerView> players, Integer verificationCode) {
    this.name = name;
    this.level = level;
    this.players = players;
    this.verificationCode = verificationCode;
  }

  public String getName() {
    return name;
  }

  public String getLevel() {
    return level;
  }

  public List<PlayerView> getPlayers() {
    return players;
  }

  public Integer getVerificationCode() {
    return verificationCode;
  }
}
