package org.fmarin.admintournoi.admin.pool;

import com.google.common.collect.Maps;
import org.fmarin.admintournoi.admin.match.MatchView;
import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.subscription.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.fmarin.admintournoi.admin.match.MatchViewBuilder.aMatchView;
import static org.fmarin.admintournoi.admin.pool.PoolDetailViewBuilder.aPoolDetailView;

@RestController
@RequestMapping("/admin/pools")
public class PoolController {

  private final PoolRepository poolRepository;

  @Autowired
  public PoolController(PoolRepository poolRepository) {
    this.poolRepository = poolRepository;
  }

  @GetMapping("/{poolId}")
  public ModelAndView get(@PathVariable(name = "poolId") Long poolId) {
    Pool pool = poolRepository.findOne(poolId);
    Round round = pool.getRound();
    Tournament tournament = round.getTournament();

    PoolDetailViewBuilder poolDetailViewBuilder = aPoolDetailView()
      .withId(pool.getId())
      .withName(pool.getFullName())
      .withField(pool.getField())
      .withTournamentId(tournament.getId())
      .withTournamentName(tournament.getFullName())
      .withRoundId(round.getId())
      .withRoundName(round.getFullName());
    if (pool.isFinished()) {
      poolDetailViewBuilder.isFinished();
    }
    PoolDetailView poolView = poolDetailViewBuilder
      .build();

    List<MatchView> matches = pool.getMatches().stream().map(match -> aMatchView()
      .withId(match.getId())
      .withTeamName1(match.getTeam1().getName())
      .withTeamName2(match.getTeam2().getName())
      .withTeamScore1(match.getScoreTeam1() != null ? match.getScoreTeam1() : 0)
      .withTeamScore2(match.getScoreTeam2() != null ? match.getScoreTeam2() : 0)
      .build())
      .collect(Collectors.toList());

    Map<String, Object> model = Maps.newHashMap();
    model.put("pool", poolView);
    model.put("matches", matches);
    model.put("rankings", pool.getRankings());
    return new ModelAndView("pool", model);
  }
}
