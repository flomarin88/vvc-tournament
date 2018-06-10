package org.fmarin.admintournoi.admin.dashboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.fmarin.admintournoi.admin.pool.Pool;
import org.fmarin.admintournoi.admin.pool.PoolView;
import org.fmarin.admintournoi.admin.pool.PoolViewBuilder;
import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.admin.round.RoundRepository;
import org.fmarin.admintournoi.admin.round.RoundStatus;
import org.fmarin.admintournoi.admin.round.RoundType;
import org.fmarin.admintournoi.admin.team.TeamOverviewView;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.fmarin.admintournoi.admin.team.TeamOverviewViewBuilder.aTeamOverviewView;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

  private final TournamentRepository tournamentRepository;
  private final RoundRepository roundRepository;

  @Autowired
  public DashboardController(TournamentRepository tournamentRepository, RoundRepository roundRepository) {
    this.tournamentRepository = tournamentRepository;
    this.roundRepository = roundRepository;
  }

  @GetMapping("/{tournamentId}")
  public ModelAndView dashboard(@PathVariable(name = "tournamentId") Long tournamentId) {
    Tournament tournament = tournamentRepository.findOne(tournamentId);
    List<Round> startedRounds = roundRepository.findAllByTournamentAndStatus(tournament, RoundStatus.STARTED);
    List<PoolView> pools = startedRounds.stream()
      .map(Round::getPools)
      .flatMap(List::stream)
      .map(this::convert)
      .sorted(Comparator.comparing(PoolView::getField))
      .collect(Collectors.toList());
    Map<String, Object> model = Maps.newHashMap();
    model.put("pools", pools);
    model.put("tournamentName", tournament.getFullName());
    return new ModelAndView("public/dashboard", model);
  }

  private PoolView convert(Pool pool) {
    PoolViewBuilder builder = PoolViewBuilder.aPoolView()
      .withField(pool.getField());
    List<TeamOverviewView> teams;
    if (pool.isFinished()) {
      teams = pool.getRankings().stream().map(ranking ->
        aTeamOverviewView()
          .withName(ranking.getTeam().getName())
          .withLetter(ranking.getPosition().toString())
          .build()).collect(Collectors.toList());
      builder.isFinished();
    } else {
      builder.withColor(pool.getRound().getBranch().getColor());
      teams = Lists.newArrayList(
        aTeamOverviewView()
          .withName(pool.getTeam1().getName())
          .withLetter("A")
          .build(),
        aTeamOverviewView()
          .withName(pool.getTeam2().getName())
          .withLetter("B")
          .build());
      if (RoundType.POOL.equals(pool.getRound().getType())) {
        teams.add(aTeamOverviewView()
          .withName(pool.getTeam3().getName())
          .withLetter("C")
          .build());
      }
    }
    builder.withTeams(teams);
    return builder.build();
  }
}
