package org.fmarin.admintournoi.admin.pool;

import com.google.common.collect.Maps;
import org.fmarin.admintournoi.admin.match.Match;
import org.fmarin.admintournoi.admin.match.MatchStatus;
import org.fmarin.admintournoi.admin.ranking.Ranking;
import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.subscription.Team;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "POOL")
public class Pool {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "round_id")
  private Round round;

  @Column(name = "position")
  private Integer position;
  @ManyToOne
  @JoinColumn(name = "team_1_id")
  private Team team1;
  @ManyToOne
  @JoinColumn(name = "team_2_id")
  private Team team2;
  @ManyToOne
  @JoinColumn(name = "team_3_id")
  private Team team3;
  @Column(name = "field")
  private Integer field;

  @OneToMany(mappedBy = "pool", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Match> matches = new ArrayList<>();

  public void addTeam(Team team) {
    if (team1 == null) {
      team1 = team;
    } else if (team2 == null) {
      team2 = team;
    } else {
      team3 = team;
    }
  }

  public void replace(Team team1, Team team2) {
    if (team1.equals(this.team1)) {
      this.team1 = team2;
    }
    if (team1.equals(this.team2)) {
      this.team2 = team2;
    }
    if (team1.equals(this.team3)) {
      this.team3 = team2;
    }
  }

  public List<Ranking> getRankings() {
    Map<Team, Ranking> rankings = Maps.newHashMap();
    for (Match match : matches) {
      updateRanking(get(rankings, match.getTeam1()), match, match.getTeam1());
      updateRanking(get(rankings, match.getTeam2()), match, match.getTeam2());
    }
    List<Ranking> rankingsOrdered = rankings.values().stream()
      .sorted(rankingComparator())
      .collect(Collectors.toList());
    updatePosition(rankingsOrdered);
    return rankingsOrdered;
  }

  public static Comparator<Ranking> rankingComparator() {
    return Comparator.comparing(Ranking::getVictories)
      .thenComparing(Ranking::getDifference)
      .thenComparing(Ranking::getPointsFor)
      .reversed();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

  public Team getTeam1() {
    return team1;
  }

  public void setTeam1(Team team1) {
    this.team1 = team1;
  }

  public Team getTeam2() {
    return team2;
  }

  public void setTeam2(Team team2) {
    this.team2 = team2;
  }

  public Team getTeam3() {
    return team3;
  }

  public void setTeam3(Team team3) {
    this.team3 = team3;
  }

  public Round getRound() {
    return round;
  }

  public void setRound(Round round) {
    this.round = round;
  }

  public List<Match> getMatches() {
    return matches;
  }

  public void setMatches(List<Match> matches) {
    this.matches = matches;
  }

  public Integer getField() {
    return field;
  }

  public void setField(Integer field) {
    this.field = field;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Pool pool = (Pool) o;
    return Objects.equals(id, pool.id);
  }

  @Override
  public int hashCode() {

    return Objects.hash(id);
  }

  private void updatePosition(List<Ranking> rankingsOrdered) {
    for (int i = 0; i < rankingsOrdered.size(); i++) {
      rankingsOrdered.get(i).setPosition(i + 1);
    }
  }

  private void updateRanking(Ranking ranking, Match match, Team team) {
    ranking.setPointsFor(ranking.getPointsFor() + match.getPointsFor(team));
    ranking.setPointsAgainst(ranking.getPointsAgainst() + match.getPointsAgainst(team));
    MatchStatus status = match.getStatus(team);
    ranking.setVictories(ranking.getVictories() + (MatchStatus.VICTORY.equals(status) ? 1 : 0));
    ranking.setDefeats(ranking.getDefeats() + (MatchStatus.DEFEAT.equals(status) ? 1 : 0));
    ranking.setDifference(ranking.getPointsFor() - ranking.getPointsAgainst());
  }

  private Ranking get(Map<Team, Ranking> rankings, Team team) {
    return rankings.computeIfAbsent(team, Ranking::new);
  }

}
