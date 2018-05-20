package org.fmarin.admintournoi.admin.round;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;

import java.util.List;

public class RoundToCreateView {

  private String name;
  private String tournamentBranch;
  private String type;
  private Long firstPreviousRoundId;
  private Integer firstTeamsFrom;
  private Integer firstTeamsTo;
  private Long secondPreviousRoundId;
  private Integer secondTeamsFrom;
  private Integer secondTeamsTo;
  private String fieldRanges;

  public List<PreviousRound> getPreviousRounds() {
    List<PreviousRound> previousRounds = Lists.newArrayList();
    if (firstPreviousRoundId != null) {
      previousRounds.add(new PreviousRound(firstPreviousRoundId, Range.closed(firstTeamsFrom, firstTeamsTo)));
      if (secondPreviousRoundId != null) {
        previousRounds.add(new PreviousRound(secondPreviousRoundId, Range.closed(secondTeamsFrom, secondTeamsTo)));
      }
    }
    return previousRounds;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTournamentBranch() {
    return tournamentBranch;
  }

  public void setTournamentBranch(String tournamentBranch) {
    this.tournamentBranch = tournamentBranch;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setFirstPreviousRoundId(Long firstPreviousRoundId) {
    this.firstPreviousRoundId = firstPreviousRoundId;
  }

  public void setFirstTeamsFrom(Integer firstTeamsFrom) {
    this.firstTeamsFrom = firstTeamsFrom;
  }

  public void setFirstTeamsTo(Integer firstTeamsTo) {
    this.firstTeamsTo = firstTeamsTo;
  }

  public String getFieldRanges() {
    return fieldRanges;
  }

  public void setFieldRanges(String fieldRanges) {
    this.fieldRanges = fieldRanges;
  }

  public void setSecondPreviousRoundId(Long secondPreviousRoundId) {
    this.secondPreviousRoundId = secondPreviousRoundId;
  }

  public void setSecondTeamsFrom(Integer secondTeamsFrom) {
    this.secondTeamsFrom = secondTeamsFrom;
  }

  public void setSecondTeamsTo(Integer secondTeamsTo) {
    this.secondTeamsTo = secondTeamsTo;
  }
}
