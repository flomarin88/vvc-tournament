package org.fmarin.admintournoi.admin.round;

import com.google.common.collect.Range;

import java.util.Objects;

public class PreviousRound {

  private final Long roundId;
  private final Range<Integer> teamsRange;

  public PreviousRound(Long roundId, Range<Integer> teamsRange) {
    this.roundId = roundId;
    this.teamsRange = teamsRange;
  }

  public Long getRoundId() {
    return roundId;
  }

  public Range<Integer> getTeamsRange() {
    return teamsRange;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PreviousRound that = (PreviousRound) o;
    return Objects.equals(roundId, that.roundId) &&
      Objects.equals(teamsRange, that.teamsRange);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roundId, teamsRange);
  }
}
