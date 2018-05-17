package org.fmarin.admintournoi.admin.pool;

import com.google.common.collect.Lists;
import org.fmarin.admintournoi.admin.ranking.Ranking;
import org.fmarin.admintournoi.admin.ranking.RankingService;
import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.admin.round.RoundRepository;
import org.fmarin.admintournoi.admin.round.RoundStatus;
import org.fmarin.admintournoi.subscription.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PoolGenerationService {

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
  public void generatePoolsWithLevels(Round round) {
    round.createPools();
    Map<Integer, List<Team>> teamsByLevel = mapTeamsByLevel(round.getTeams());
    affectTeams(round, teamsByLevel);
  }

  @Async
  public void generatePoolsWithRankings(Round round) {
    round.createPools();
    List<Ranking> rankings = getAllPoolRankings(round);
    Map<Integer, List<Team>> teamsByRanking = mapTeamsByRanking(rankings);
    affectTeams(round, teamsByRanking);
  }

  Pool getPool(Round round, int affectedTeams) {
    int index = affectedTeams % round.getPools().size();
    return round.getPools().get(index);
  }

  Team getTeamToAffect(Map<Integer, List<Team>> teamsMap, List<Integer> positions) {
    Optional<Integer> keyToAffect = positions.stream().filter(key-> !teamsMap.get(key).isEmpty()).findFirst();
    if (keyToAffect.isPresent()) {
      List<Team> teams = teamsMap.get(keyToAffect.get());
      Random random = new Random();
      int index = random.ints(0, teams.size()).findFirst().getAsInt();
      return teams.remove(index);
    } else {
      throw new RuntimeException("Generation failed - No team to affect");
    }
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

  Map<Integer, List<Team>> mapTeamsByLevel(List<Team> teams) {
    return teams.parallelStream().collect(Collectors.groupingBy(team -> team.getLevel().getValue(), Collectors.toList()));
  }

  Map<Integer, List<Team>> mapTeamsByRanking(List<Ranking> rankings) {
    Map<Integer, List<Ranking>> rankingsByPosition = rankings.parallelStream()
      .collect(Collectors.groupingBy(Ranking::getPosition, Collectors.toList()));
    return rankingsByPosition.entrySet().stream()
      .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().map(Ranking::getTeam).collect(Collectors.toList())));
  }

  private void affectTeams(Round round, Map<Integer, List<Team>> teamsMap) {
    Set<Integer> positions = teamsMap.keySet();
    for (int i = 0; i < round.getTeams().size(); ++i) {
      int loopCount = i / round.getPools().size() + 1;
      Pool pool = getPool(round, i);
      Team team = getTeamToAffect(teamsMap, orderLevels(positions, loopCount));
      pool.addTeam(team);
    }
    round.getPools().parallelStream().forEach(poolRepository::save);
    round.setStatus(RoundStatus.COMPOSED);
    roundRepository.save(round);
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
}
