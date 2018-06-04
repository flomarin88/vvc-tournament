package org.fmarin.admintournoi.rounds;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.fmarin.admintournoi.admin.pool.Pool;
import org.fmarin.admintournoi.admin.pool.PoolRepository;
import org.fmarin.admintournoi.admin.pool.PoolView;
import org.fmarin.admintournoi.admin.pool.PoolViewBuilder;
import org.fmarin.admintournoi.admin.round.*;
import org.fmarin.admintournoi.admin.team.TeamOverviewView;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

import static org.fmarin.admintournoi.admin.team.TeamOverviewViewBuilder.aTeamOverviewView;

@RestController
public class RoundPublicController {

  private final RoundRepository roundRepository;
  private final PoolRepository poolRepository;
  private final TournamentRepository tournamentRepository;


  @Autowired
  public RoundPublicController(RoundRepository roundRepository, PoolRepository poolRepository, TournamentRepository tournamentRepository) {
    this.roundRepository = roundRepository;
    this.poolRepository = poolRepository;
    this.tournamentRepository = tournamentRepository;
  }

  @GetMapping("/tournaments/{tournamentId}/rounds")
  public ModelAndView index(@PathVariable(name = "tournamentId") Long tournamentId) {
    Tournament tournament = tournamentRepository.findOne(tournamentId);
    Map<String, Object> model = Maps.newHashMap();
    model.put("tournamentId", tournament.getId());
    model.put("tournamentName", tournament.getFullName());
    List<Round> startedRounds = roundRepository.findAllByTournamentAndStatus(tournament, RoundStatus.STARTED);
    List<RoundPublicView> roundViews = Arrays.stream(TournamentBranch.values())
      .map(branch -> getRound(startedRounds, branch).map(round -> new RoundPublicView(branch.name().toLowerCase(), branch.getColor(), round.getFullName(), false))
        .orElseGet(() -> new RoundPublicView(branch.name().toLowerCase(), branch.getColor(), branch.getLabel() + " - Aucun tour", true))).collect(Collectors.toList());
    model.put("rounds", roundViews);
    return new ModelAndView("public/rounds", model);
  }

  @GetMapping("/tournaments/{tournamentId}/rounds/{roundBranch}")
  public ModelAndView getRound(@PathVariable(name = "tournamentId") Long tournamentId, @PathVariable(name = "roundBranch") String roundBranch) {
    Tournament tournament = tournamentRepository.findOne(tournamentId);
    Map<String, Object> model = Maps.newHashMap();
    model.put("tournamentId", tournament.getId());
    model.put("tournamentName", tournament.getFullName());
    List<Round> startedRounds = roundRepository.findAllByTournamentAndStatus(tournament, RoundStatus.STARTED);
    Optional<Round> round = getRound(startedRounds, TournamentBranch.valueOf(roundBranch.toUpperCase()));
    if (round.isPresent()) {
      model.put("name", round.get().getFullName());
      List<PoolView> pools = poolRepository.findAllByRoundOrderByPosition(round.get()).stream()
        .map(this::convert).collect(Collectors.toList());
      model.put("pools", pools);
    }
    return new ModelAndView("public/round", model);
  }

  private Optional<Round> getRound(List<Round> startedRounds, TournamentBranch branch) {
    return startedRounds.parallelStream().filter(round -> round.getBranch().equals(branch)).findFirst();
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
