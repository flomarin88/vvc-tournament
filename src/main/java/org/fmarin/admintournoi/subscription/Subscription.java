package org.fmarin.admintournoi.subscription;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Subscription {

  @NotNull
  @Min(1)
  private Long tournamentId = 0L;
  @NotEmpty
  private String name = "";
  @NotNull
  @Min(1)
  @Max(4)
  private int level = 0;
  @NotEmpty
  private String captainName = "";
  @NotEmpty
  @Email
  private String captainEmail = "";
  private String captainClub = "";
  @NotEmpty
  private String captainPhone = "";
  @NotEmpty
  private String player2Name = "";
  private String player2Email = "";
  private String player2Club = "";
  @NotEmpty
  private String player3Name = "";
  private String player3Email = "";
  private String player3Club = "";

  public Long getTournamentId() {
    return tournamentId;
  }

  public void setTournamentId(Long tournamentId) {
    this.tournamentId = tournamentId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public String getCaptainName() {
    return captainName;
  }

  public void setCaptainName(String captainName) {
    this.captainName = captainName;
  }

  public String getCaptainEmail() {
    return captainEmail;
  }

  public void setCaptainEmail(String captainEmail) {
    this.captainEmail = captainEmail;
  }

  public String getCaptainPhone() {
    return captainPhone;
  }

  public void setCaptainPhone(String captainPhone) {
    this.captainPhone = captainPhone;
  }

  public String getPlayer2Name() {
    return player2Name;
  }

  public void setPlayer2Name(String player2Name) {
    this.player2Name = player2Name;
  }

  public String getPlayer2Email() {
    return player2Email;
  }

  public void setPlayer2Email(String player2Email) {
    this.player2Email = player2Email;
  }

  public String getPlayer3Name() {
    return player3Name;
  }

  public void setPlayer3Name(String player3Name) {
    this.player3Name = player3Name;
  }

  public String getPlayer3Email() {
    return player3Email;
  }

  public void setPlayer3Email(String player3Email) {
    this.player3Email = player3Email;
  }

  public String getCaptainClub() {
    return captainClub;
  }

  public void setCaptainClub(String captainClub) {
    this.captainClub = captainClub;
  }

  public String getPlayer2Club() {
    return player2Club;
  }

  public void setPlayer2Club(String player2Club) {
    this.player2Club = player2Club;
  }

  public String getPlayer3Club() {
    return player3Club;
  }

  public void setPlayer3Club(String player3Club) {
    this.player3Club = player3Club;
  }
}
