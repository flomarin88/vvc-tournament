package org.fmarin.admintournoi.admin.ranking;

import com.google.common.collect.Maps;
import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.admin.round.RoundListView;
import org.fmarin.admintournoi.admin.round.RoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

import static org.fmarin.admintournoi.admin.round.RoundListViewBuilder.aRoundListView;

@RestController
@RequestMapping("/admin")
public class RankingController {

  private final RankingService rankingService;
  private final RoundRepository roundRepository;

  @Autowired
  public RankingController(RankingService rankingService, RoundRepository roundRepository) {
    this.rankingService = rankingService;
    this.roundRepository = roundRepository;
  }

  @GetMapping("/pools/{id}/rankings")
  public ResponseEntity getPoolRankings(@PathVariable(name = "id") Long poolId) {
    return ResponseEntity.ok(rankingService.getPoolRanking(poolId));
  }

  @GetMapping("/rounds/{id}/rankings")
  public ModelAndView getRoundRankings(@PathVariable(name = "id") Long roundId) {
    Round round = roundRepository.findOne(roundId);
    Map<String, Object> model = Maps.newHashMap();
    RoundListView roundView = aRoundListView()
      .withId(roundId)
      .withName(round.getBranch().getLabel() + " - " + round.getName())
      .withTournamentId(round.getTournament().getId())
      .withTournamentName(round.getTournament().getFullName())
      .build();
    model.put("round", roundView);
    model.put("rankings", rankingService.getRoundRanking(roundId));
    return new ModelAndView("rankings", model);
  }
}
