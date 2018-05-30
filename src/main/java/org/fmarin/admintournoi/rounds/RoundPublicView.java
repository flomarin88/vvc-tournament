package org.fmarin.admintournoi.rounds;

import java.util.Objects;

public class RoundPublicView {

  private final String branch;
  private final String color;
  private final String name;
  private final boolean disabled;

  public RoundPublicView(String branch, String color, String name, boolean disabled) {
    this.branch = branch;
    this.color = color;
    this.name = name;
    this.disabled = disabled;
  }

  public String getBranch() {
    return branch;
  }

  public String getColor() {
    return color;
  }

  public String getName() {
    return name;
  }

  public boolean isDisabled() {
    return disabled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RoundPublicView that = (RoundPublicView) o;
    return disabled == that.disabled &&
      Objects.equals(branch, that.branch) &&
      Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(branch, name, disabled);
  }
}
