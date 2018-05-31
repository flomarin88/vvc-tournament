package org.fmarin.admintournoi.admin.round;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.fmarin.admintournoi.admin.pool.Pool;
import org.fmarin.admintournoi.admin.pool.PoolBuilder;
import org.fmarin.admintournoi.admin.pool.TeamOpposition;
import org.fmarin.admintournoi.admin.ranking.Ranking;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.Tournament;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Entity
@Table(name = "ROUND")
public class Round {

  public static Integer TEAMS_COUNT_BY_POOL = 3;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "name")
  private String name;
  @OneToMany(mappedBy = "round", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  private List<PreviousRound> previousRounds = Lists.newArrayList();
  @ManyToOne
  @JoinColumn(name = "tournament_id")
  private Tournament tournament;
  @Column(name = "tournament_branch")
  @Enumerated(value = EnumType.STRING)
  private TournamentBranch branch;
  @ManyToMany(cascade = CascadeType.MERGE)
  @JoinTable(name = "ROUND_TEAM",
    joinColumns = @JoinColumn(name = "round_id", referencedColumnName = "ID"),
    inverseJoinColumns = @JoinColumn(name = "team_id", referencedColumnName = "ID"))
  private List<Team> teams = Lists.newArrayList();
  @Column(name = "status")
  @Enumerated(value = EnumType.STRING)
  private RoundStatus status;

  @Column(name = "type")
  @Enumerated(value = EnumType.STRING)
  private RoundType type;

  @OneToMany(mappedBy = "round", fetch = FetchType.EAGER)
  @Fetch(value = FetchMode.SELECT)
  private List<Pool> pools = Lists.newArrayList();

  @Column(name = "field_ranges")
  private String fieldRanges;

  @Transient
  private List<Integer> fields;

  public void createPools() {
    Integer poolsCount = teams.size() / TEAMS_COUNT_BY_POOL;
    this.pools = IntStream.range(0, poolsCount).mapToObj(index ->
      PoolBuilder.aPool()
        .withField(getField(index + 1))
        .withPosition(index + 1)
        .withRound(this)
        .build()
    ).collect(Collectors.toList());
  }

  public Set<TeamOpposition> getOppositions() {
    Set<TeamOpposition> oppositions = Sets.newHashSet();
    for (Pool pool : pools) {
      oppositions.add(new TeamOpposition(pool.getTeam1(), pool.getTeam2()));
      oppositions.add(new TeamOpposition(pool.getTeam1(), pool.getTeam3()));
      oppositions.add(new TeamOpposition(pool.getTeam2(), pool.getTeam3()));
    }
    return oppositions;
  }

  public List<Ranking> getRankings() {
    List<Ranking> rankingsByPool = pools.parallelStream()
      .map(Pool::getRankings)
      .flatMap(List::stream)
      .collect(Collectors.toList());
    rankingsByPool.sort(rankingComparator());
    updatePosition(rankingsByPool);
    return rankingsByPool;
  }

  public String getFullName() {
    return branch.getLabel() + " - " + name;
  }

  public boolean isStarted() {
    return RoundStatus.STARTED.equals(status);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Tournament getTournament() {
    return tournament;
  }

  public void setTournament(Tournament tournament) {
    this.tournament = tournament;
  }

  public List<Team> getTeams() {
    return teams;
  }

  public void setTeams(List<Team> teams) {
    this.teams = teams;
  }

  public List<PreviousRound> getPreviousRounds() {
    return previousRounds;
  }

  public void setPreviousRounds(List<PreviousRound> previousRounds) {
    this.previousRounds = previousRounds;
  }

  public RoundStatus getStatus() {
    return status;
  }

  public void setStatus(RoundStatus status) {
    this.status = status;
  }

  public TournamentBranch getBranch() {
    return branch;
  }

  public void setBranch(TournamentBranch branch) {
    this.branch = branch;
  }

  public RoundType getType() {
    return type;
  }

  public void setType(RoundType type) {
    this.type = type;
  }

  public List<Pool> getPools() {
    return pools;
  }

  public void setPools(List<Pool> pools) {
    this.pools = pools;
  }

  public String getFieldRanges() {
    return fieldRanges;
  }

  public void setFieldRanges(String fieldRanges) {
    this.fieldRanges = fieldRanges;
    this.fields = getFields();
  }

  List<Integer> getFields() {
    List<Integer> result = Lists.newArrayList();
    String[] ranges = fieldRanges.split(";");
    for (String range1 : ranges) {
      String[] range = range1.split("-");
      Integer from = Integer.valueOf(range[0]);
      Integer to = Integer.valueOf(range[1]);
      result.addAll(IntStream.range(from, to + 1).boxed().collect(Collectors.toList()));
    }
    return result;
  }

  Integer getField(Integer position) {
    Integer newPosition = position % fields.size();
    if (newPosition == 0) {
      newPosition = fields.size();
    }
    return fields.get(newPosition - 1);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Round round = (Round) o;
    return Objects.equals(id, round.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  private Comparator<Ranking> rankingComparator() {
    return Comparator.comparing(Ranking::getPosition)
      .thenComparing(Pool.rankingComparator());
  }

  private void updatePosition(List<Ranking> rankingsOrdered) {
    for (int i = 0; i < rankingsOrdered.size(); i++) {
      rankingsOrdered.get(i).setPosition(i + 1);
    }
  }
}
