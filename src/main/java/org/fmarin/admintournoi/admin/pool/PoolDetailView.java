package org.fmarin.admintournoi.admin.pool;

public class PoolDetailView {

  private final Long id;
  private final Integer field;
  private final String name;
  private final Long tournamentId;
  private final String tournamentName;
  private final Long roundId;
  private final String roundName;
  private final boolean finished;

  public PoolDetailView(Long id, Integer field, String name, Long tournamentId, String tournamentName, Long roundId, String roundName, boolean finished) {
    this.id = id;
    this.field = field;
    this.name = name;
    this.tournamentId = tournamentId;
    this.tournamentName = tournamentName;
    this.roundId = roundId;
    this.roundName = roundName;
    this.finished = finished;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Long getTournamentId() {
    return tournamentId;
  }

  public String getTournamentName() {
    return tournamentName;
  }

  public Long getRoundId() {
    return roundId;
  }

  public String getRoundName() {
    return roundName;
  }


  public Integer getField() {
    return field;
  }

  public boolean isFinished() {
    return finished;
  }
}
