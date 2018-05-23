package org.fmarin.admintournoi.admin.ranking;

import org.fmarin.admintournoi.admin.pool.Pool;
import org.fmarin.admintournoi.admin.pool.PoolRepository;
import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.admin.round.RoundRepository;
import org.fmarin.admintournoi.subscription.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RankingService {

  private final PoolRepository poolRepository;

  @Autowired
  public RankingService(PoolRepository poolRepository, RoundRepository roundRepository) {
    this.poolRepository = poolRepository;
  }

  public Ranking getTeamRanking(Team team, Round round) {
    Pool pool = poolRepository.findByRoundAndTeam(round, team);
    return pool.getRanking(team);
  }
}
