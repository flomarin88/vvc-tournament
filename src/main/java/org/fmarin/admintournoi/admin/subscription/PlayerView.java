package org.fmarin.admintournoi.admin.subscription;

public class PlayerView {

  private String name;
  private String club;
  private String email;
  private String phone;

  public PlayerView(String name, String club, String email, String phone) {
    this.name = name;
    this.club = club;
    this.email = email;
    this.phone = phone;
  }

  public String getName() {
    return name;
  }

  public String getClub() {
    return club;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone() {
    return phone;
  }
}
