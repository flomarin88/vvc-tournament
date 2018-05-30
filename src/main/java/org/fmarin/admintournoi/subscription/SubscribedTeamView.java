package org.fmarin.admintournoi.subscription;

public class SubscribedTeamView {

  private final Integer index;
  private final String name;

  public SubscribedTeamView(Integer index, String name) {
    this.index = index;
    this.name = name;
  }

  public Integer getIndex() {
    return index;
  }

  public String getName() {
    return name;
  }
}
