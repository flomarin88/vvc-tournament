package org.fmarin.admintournoi.admin.ranking;

import com.google.common.collect.Maps;
import org.fmarin.admintournoi.admin.pool.Pool;
import org.fmarin.admintournoi.admin.pool.PoolRepository;
import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.admin.round.RoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class RankingController {

  private final RoundRepository roundRepository;
  private final PoolRepository poolRepository;

  @Autowired
  public RankingController(RoundRepository roundRepository, PoolRepository poolRepository) {
    this.roundRepository = roundRepository;
    this.poolRepository = poolRepository;
  }

  @GetMapping("/pools/{id}/rankings")
  public ResponseEntity getPoolRankings(@PathVariable(name = "id") Long poolId) {
    Pool pool = poolRepository.findOne(poolId);
    return ResponseEntity.ok(pool.getRankings());
  }

  @GetMapping("/rounds/{id}/rankings")
  public ModelAndView getRoundRankings(@PathVariable(name = "id") Long roundId) {
    Round round = roundRepository.findOne(roundId);
    Map<String, Object> model = Maps.newHashMap();
    model.put("tournament", round.getTournament());
    model.put("round", round);
    model.put("rankings", round.getRankings());
    return new ModelAndView("rankings", model);
  }
}
