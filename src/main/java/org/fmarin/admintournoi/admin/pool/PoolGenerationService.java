package org.fmarin.admintournoi.admin.pool;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.fmarin.admintournoi.admin.ranking.Ranking;
import org.fmarin.admintournoi.admin.ranking.RankingService;
import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.admin.round.RoundRepository;
import org.fmarin.admintournoi.admin.round.RoundStatus;
import org.fmarin.admintournoi.subscription.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PoolGenerationService {

  private static final Logger logger = LoggerFactory.getLogger(PoolGenerationService.class);

  private static Integer TEAMS_COUNT_BY_POOL = 3;

  private final PoolRepository poolRepository;
  private final RoundRepository roundRepository;
  private final RankingService rankingService;

  @Autowired
  public PoolGenerationService(PoolRepository poolRepository, RoundRepository roundRepository, RankingService rankingService) {
    this.poolRepository = poolRepository;
    this.roundRepository = roundRepository;
    this.rankingService = rankingService;
  }

  @Async
  public void generatePools(Round round) {
    round.createPools();
    affectTeams(round, groupTeams(round));
  }

  private void affectTeams(Round round, Map<Integer, List<Team>> teamsMap) {
    logger.info("Affect teams for round {}", round.getId());
    Set<Integer> positions = teamsMap.keySet();
    Set<TeamOpposition> oppositions = getPreviousOppositions(round);
    for (int i = 0; i < round.getTeams().size(); ++i) {
      int loopCount = i / round.getPools().size() + 1;
      Pool pool = getPool(round, i);
      Team team = getTeamToAffect(teamsMap, orderLevels(positions, loopCount), oppositions, pool);
      if (team == null) {
        generatePools(round);
      } else {
        pool.addTeam(team);
      }
    }
    round.getPools().parallelStream().forEach(poolRepository::save);
    round.setStatus(RoundStatus.COMPOSED);
    roundRepository.save(round);
  }

  private Map<Integer, List<Team>> groupTeams(Round round) {
    if (round.getPreviousRound() == null) {
      return mapTeamsByLevel(round.getTeams());
    } else {
      List<Ranking> rankings = getAllPoolRankings(round);
      return mapTeamsByRanking(rankings);
    }
  }

  Map<Integer, List<Team>> mapTeamsByLevel(List<Team> teams) {
    return teams.parallelStream().collect(Collectors.groupingBy(team -> team.getLevel().getValue(), Collectors.toList()));
  }

  Map<Integer, List<Team>> mapTeamsByRanking(List<Ranking> rankings) {
    Map<Integer, List<Ranking>> rankingsByPosition = rankings.parallelStream()
      .collect(Collectors.groupingBy(Ranking::getPosition, Collectors.toList()));
    return rankingsByPosition.entrySet().stream()
      .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().map(Ranking::getTeam).collect(Collectors.toList())));
  }

  Pool getPool(Round round, int affectedTeams) {
    int index = affectedTeams % round.getPools().size();
    return round.getPools().get(index);
  }

  List<Integer> orderLevels(Set<Integer> levels, int loopCount) {
    LinkedList<Integer> orderedLevels = new LinkedList<>(levels);
    orderedLevels.sort(Comparator.naturalOrder());
    if (loopCount == TEAMS_COUNT_BY_POOL) {
      return Lists.reverse(orderedLevels);
    }
    return orderedLevels;
  }

  Map<Integer, Long> countTeamsByLevel(List<Team> teams) {
    return teams.parallelStream().collect(Collectors.groupingBy(team -> team.getLevel().getValue(), Collectors.counting()));
  }

  boolean hasAlreadyPlayedAgainst(Team team, Pool pool, Set<TeamOpposition> oppositions) {
    if (pool.getTeam1() != null) {
      boolean team1 = contains(new TeamOpposition(team, pool.getTeam1()), oppositions);
      if (!team1 && pool.getTeam2() != null) {
        return contains(new TeamOpposition(team, pool.getTeam2()), oppositions);
      }
      return team1;
    }
    return false;
  }

  boolean contains(TeamOpposition teamOpposition, Set<TeamOpposition> oppositions) {
    Optional<TeamOpposition> any = oppositions.parallelStream().filter(opposition -> opposition.equals(teamOpposition)).findAny();
    return any.isPresent();
  }

  Set<TeamOpposition> getPreviousOppositions(Round round) {
    Set<TeamOpposition> oppositions = Sets.newHashSet();
    if (round != null && round.getPreviousRound() != null) {
      Round previousRound = roundRepository.findOne(round.getPreviousRound().getId());
      oppositions.addAll(previousRound.getOppositions());
      oppositions.addAll(getPreviousOppositions(previousRound));
    }
    return oppositions;
  }

  private List<Ranking> getAllPoolRankings(Round round) {
    return round.getPreviousRound().getPools().stream()
      .map(pool -> rankingService.getPoolRanking(pool.getId()))
      .collect(Collectors.toList()).stream()
      .flatMap(List::stream)
      .collect(Collectors.toList())
      .stream().filter(ranking -> round.getTeams().contains(ranking.getTeam()))
      .collect(Collectors.toList());
  }

  private Team getTeamToAffect(Map<Integer, List<Team>> teamsMap, List<Integer> positions, Set<TeamOpposition> oppositions, Pool pool) {
    Optional<Integer> keyToAffect = positions.stream().filter(key -> !teamsMap.get(key).isEmpty()).findFirst();
    List<Team> teams = teamsMap.get(keyToAffect.get());
    Team team = getTeam(teams, teams.size(), pool, oppositions);
    if (team == null) {
      logger.info("Impossible to affect team");
    }
    return team;
  }

  private Team getTeam(List<Team> teams, int count, Pool pool, Set<TeamOpposition> oppositions) {
    if (count == 0) {
      return null;
    }
    Random random = new Random();
    int index = random.ints(0, count).findFirst().getAsInt();
    Team team = teams.remove(index);
    if (hasAlreadyPlayedAgainst(team, pool, oppositions)) {
      teams.add(team);
      return getTeam(teams, count - 1, pool, oppositions);
    }
    return team;

  }
}
