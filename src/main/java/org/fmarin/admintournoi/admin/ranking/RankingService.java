package org.fmarin.admintournoi.admin.ranking;

import org.fmarin.admintournoi.admin.pool.Pool;
import org.fmarin.admintournoi.admin.pool.PoolRepository;
import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.admin.round.RoundRepository;
import org.fmarin.admintournoi.subscription.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class RankingService {

  private final PoolRepository poolRepository;
  private final RoundRepository roundRepository;

  @Autowired
  public RankingService(PoolRepository poolRepository, RoundRepository roundRepository) {
    this.poolRepository = poolRepository;
    this.roundRepository = roundRepository;
  }

  public List<Ranking> getRoundRanking(Long roundId) {
    Round round = roundRepository.findOne(roundId);
    List<Ranking> rankingsByPool = round.getPools().parallelStream()
      .map(Pool::getRankings)
      .collect(Collectors.toList()).stream()
      .flatMap(List::stream)
      .collect(Collectors.toList());
    rankingsByPool.sort(roundRankingOrdering());
    updatePosition(rankingsByPool);
    return rankingsByPool;
  }

  public Ranking getTeamRanking(Team team, Round round) {
    Pool pool = poolRepository.findByRoundAndTeam(round, team);
    List<Ranking> rankings = pool.getRankings();
    Optional<Ranking> optRanking = rankings.stream().filter(ranking -> ranking.getTeam().equals(team)).findAny();
    return optRanking.get();
  }

  private void updatePosition(List<Ranking> rankingsOrdered) {
    for (int i = 0; i < rankingsOrdered.size(); i++) {
      rankingsOrdered.get(i).setPosition(i + 1);
    }
  }

  private Comparator<Ranking> roundRankingOrdering() {
    return Comparator.comparing(Ranking::getPosition)
      .thenComparing(Pool.rankingComparator());
  }
}
