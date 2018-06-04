package org.fmarin.admintournoi.admin.pool;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.fmarin.admintournoi.admin.ranking.Ranking;
import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.admin.round.RoundRepository;
import org.fmarin.admintournoi.admin.round.RoundStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DirectEliminationGenerationService {

  private final RoundRepository roundRepository;

  private final List<Integer> SEMI = Lists.newArrayList(0, 1);
  private final List<Integer> QUARTER = Lists.newArrayList(0, 3, 2, 1);
  private final List<Integer> EIGHTH = Lists.newArrayList(0, 7, 4, 3, 2, 5, 6, 1);

  @Autowired
  public DirectEliminationGenerationService(RoundRepository roundRepository) {
    this.roundRepository = roundRepository;
  }

  @Async
  public void generatePools(Round round) {
    round.createPools();
    List<Ranking> rankings = getPreviousRankings(round);
    affectTeams(round, rankings);
    round.setStatus(RoundStatus.COMPOSED);
    roundRepository.save(round);
  }

  void affectTeams(Round round, List<Ranking> rankings) {
    for (int i = 0; i < round.getPools().size(); i++) {
      Pool pool = get(round, i);
      pool.addTeam(rankings.remove(0).getTeam());
      pool.addTeam(rankings.remove(rankings.size() - 1).getTeam());
    }
  }

  private List<Ranking> getPreviousRankings(Round round) {
    return round.getPreviousRounds().parallelStream()
      .map(previousRound -> previousRound.getPreviousRound().getRankings())
      .flatMap(List::stream)
      .filter(ranking -> round.getTeams().contains(ranking.getTeam()))
      .collect(Collectors.toList());
  }

  private Pool get(Round round, int index) {
    List<Pool> pools = round.getPools();
    Integer poolIndex = poolsRepartition().get(pools.size()).get(index);
    return pools.get(poolIndex);
  }

  private Map<Integer, List<Integer>> poolsRepartition() {
    Map<Integer, List<Integer>> repartition = Maps.newHashMap();
    repartition.put(2, SEMI);
    repartition.put(4, QUARTER);
    repartition.put(8, EIGHTH);
    return repartition;
  }
}
