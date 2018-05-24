package org.fmarin.admintournoi.admin.round;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "PREVIOUS_ROUND")
public class PreviousRound {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @ManyToOne
  @JoinColumn(name = "previous_round_id")
  private Round previousRound;
  @ManyToOne
  @JoinColumn(name = "round_id")
  private Round round;
  @Column(name = "teams_from")
  private Integer teamsFrom;
  @Column(name = "teams_to")
  private Integer teamsTo;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Round getPreviousRound() {
    return previousRound;
  }

  public Round getRound() {
    return round;
  }

  public Integer getTeamsFrom() {
    return teamsFrom;
  }

  public Integer getTeamsTo() {
    return teamsTo;
  }

  public void setPreviousRound(Round previousRound) {
    this.previousRound = previousRound;
  }

  public void setRound(Round round) {
    this.round = round;
  }

  public void setTeamsFrom(Integer teamsFrom) {
    this.teamsFrom = teamsFrom;
  }

  public void setTeamsTo(Integer teamsTo) {
    this.teamsTo = teamsTo;
  }

  public String getLabel() {
    return previousRound.getBranch().getLabel() + " - " + previousRound.getName();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PreviousRound that = (PreviousRound) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {

    return Objects.hash(id);
  }
}
