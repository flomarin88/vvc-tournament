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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    buildPools(round);
    Map<Integer, List<Team>> teamsByLevel = mapTeamsByLevel(round.getTeams());
    affectTeams(round, teamsByLevel);
  }

  @Async
  public void generatePoolsWithRankings(Round round) {
    buildPools(round);
    List<Ranking> rankings = getAllPoolRankings(round);
    Map<Integer, List<Team>> teamsByRanking = mapTeamsByRanking(rankings);
    affectTeams(round, teamsByRanking);
  }

  void buildPools(Round round) {
    List<Integer> fields = getFieldList(round.getFieldRanges());
    Integer poolsCount = round.getTeams().size() / TEAMS_COUNT_BY_POOL;
    List<Pool> pools = IntStream.range(0, poolsCount).mapToObj(index ->
      PoolBuilder.aPool()
        .withField(getField(fields, index + 1))
        .withPosition(index + 1)
        .withRound(round)
        .build()
    ).collect(Collectors.toList());
    round.setPools(pools);
  }

  Pool getPool(Round round, int affectedTeams) {
    int index = affectedTeams % round.getPools().size();
    return round.getPools().get(index);
  }

  Team getTeamToAffect(Map<Integer, List<Team>> teamsMap) {
    Optional<Integer> keyToAffect = teamsMap.keySet().stream().filter(key-> !teamsMap.get(key).isEmpty()).findFirst();
    if (keyToAffect.isPresent()) {
      List<Team> teams = teamsMap.get(keyToAffect.get());
      Random random = new Random();
      int index = random.ints(0, teams.size()).findFirst().getAsInt();
      return teams.remove(index);
    } else {
      throw new RuntimeException("Generation failed - No team to affect");
    }
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

  Integer getField(List<Integer> fields, Integer position) {
    Integer newPosition = position % fields.size();
    if (newPosition == 0) {
      newPosition = fields.size();
    }
    return fields.get(newPosition - 1);
  }

  List<Integer> getFieldList(String fieldRanges) {
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

  private void affectTeams(Round round, Map<Integer, List<Team>> teamsMap) {
    for (int i = 0; i < round.getTeams().size(); ++i) {
      Pool pool = getPool(round, i);
      pool.addTeam(getTeamToAffect(teamsMap));
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
