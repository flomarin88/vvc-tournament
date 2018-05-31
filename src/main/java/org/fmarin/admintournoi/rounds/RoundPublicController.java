package org.fmarin.admintournoi.rounds;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.fmarin.admintournoi.admin.pool.Pool;
import org.fmarin.admintournoi.admin.pool.PoolRepository;
import org.fmarin.admintournoi.admin.pool.PoolView;
import org.fmarin.admintournoi.admin.pool.PoolViewBuilder;
import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.admin.round.RoundRepository;
import org.fmarin.admintournoi.admin.round.RoundStatus;
import org.fmarin.admintournoi.admin.round.TournamentBranch;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    return new ModelAndView("/public/rounds", model);
  }

  private Optional<Round> getRound(List<Round> startedRounds, TournamentBranch branch) {
    return startedRounds.parallelStream().filter(round -> round.getBranch().equals(branch)).findFirst();
  }

  @GetMapping("/rounds/{roundId}")
  public ModelAndView getRound(@PathVariable(name = "roundId") Long roundId) {
    Round round = roundRepository.findOne(roundId);
    List<PoolView> pools = poolRepository.findAllByRoundOrderByPosition(round).stream()
      .map(this::convert).collect(Collectors.toList());
    Map<String, Object> model = Maps.newHashMap();
    Map<String, Object> roundMap = Maps.newHashMap();
    roundMap.put("name", round.getBranch().getLabel() + " / " + round.getName());
    roundMap.put("tournamentName", round.getTournament().getFullName());
    model.put("round", roundMap);
    model.put("pools", pools);
    return new ModelAndView("round_detail_public", model);
  }

  private PoolView convert(Pool pool) {
    return PoolViewBuilder.aPoolView()
      .withId(pool.getId())
      .withName("Poule " + pool.getPosition())
      .withField(pool.getField())
      .withTeams(Lists.newArrayList(
        aTeamOverviewView()
          .withName(pool.getTeam1().getName())
          .withLetter("A")
          .build(),
        aTeamOverviewView()
          .withName(pool.getTeam2().getName())
          .withLetter("B")
          .build(),
        aTeamOverviewView()
          .withName(pool.getTeam3().getName())
          .withLetter("C")
          .build()
      ))
      .build();
  }
}
