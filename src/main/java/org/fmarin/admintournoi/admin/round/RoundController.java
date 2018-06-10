package org.fmarin.admintournoi.admin.round;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.fmarin.admintournoi.MainProperties;
import org.fmarin.admintournoi.admin.match.MatchGenerationService;
import org.fmarin.admintournoi.admin.pool.Pool;
import org.fmarin.admintournoi.admin.pool.PoolRepository;
import org.fmarin.admintournoi.admin.pool.PoolView;
import org.fmarin.admintournoi.admin.pool.PoolViewBuilder;
import org.fmarin.admintournoi.admin.team.TeamOverviewView;
import org.fmarin.admintournoi.admin.team.TeamOverviewViewBuilder;
import org.fmarin.admintournoi.admin.team.TeamService;
import org.fmarin.admintournoi.admin.tools.GeneratorService;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.TeamRepository;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.fmarin.admintournoi.admin.pool.PoolViewBuilder.aPoolView;
import static org.fmarin.admintournoi.admin.team.TeamOverviewViewBuilder.aTeamOverviewView;

@RestController
@RequestMapping("/admin")
public class RoundController {

  private final TournamentRepository tournamentRepository;
  private final TeamRepository teamRepository;
  private final RoundRepository roundRepository;
  private final PoolRepository poolRepository;
  private final MatchGenerationService matchGenerationService;
  private final RoundService roundService;
  private final TeamService teamService;
  private final GeneratorService generatorService;
  private final MainProperties mainProperties;

  private static final Logger logger = LoggerFactory.getLogger(RoundController.class);

  @Autowired
  public RoundController(TournamentRepository tournamentRepository, TeamRepository teamRepository,
                         RoundRepository roundRepository, PoolRepository poolRepository,
                         MatchGenerationService matchGenerationService, RoundService roundService,
                         TeamService teamService, GeneratorService generatorService, MainProperties mainProperties) {
    this.tournamentRepository = tournamentRepository;
    this.teamRepository = teamRepository;
    this.roundRepository = roundRepository;
    this.poolRepository = poolRepository;
    this.matchGenerationService = matchGenerationService;
    this.roundService = roundService;
    this.teamService = teamService;
    this.generatorService = generatorService;
    this.mainProperties = mainProperties;
  }

  @GetMapping("/tournaments/{tournamentId}/rounds")
  public ModelAndView index(@PathVariable(name = "tournamentId") Long tournamentId) {
    Tournament tournament = tournamentRepository.findOne(tournamentId);
    LinkedHashMap<Integer, List<RoundListView>> rounds = roundRepository.findAllByTournament(tournament)
      .parallelStream()
      .sorted(Comparator.comparing(Round::getLevel).reversed().thenComparing(round -> round.getBranch().getOrder()))
      .collect(Collectors.groupingBy(Round::getLevel, LinkedHashMap::new, Collectors.mapping(o -> convertListItem(o, false), toList())));
    Map<String, Object> model = Maps.newHashMap();
    model.put("tournament", tournament);
    model.put("rounds", rounds.entrySet());
    return new ModelAndView("round_index", model);
  }

  @GetMapping("/tournaments/{tournamentId}/rounds/new")
  public ModelAndView newRound(@Valid @PathVariable(name = "tournamentId") Long tournamentId) {
    Tournament tournament = tournamentRepository.findOne(tournamentId);
    List<Round> rounds = roundRepository.findAllByTournament(tournament);
    Map<String, Object> model = Maps.newHashMap();
    model.put("tournament", tournament);
    model.put("rounds", rounds);
    return new ModelAndView("round_new", model);
  }

  @PostMapping("/tournaments/{tournamentId}/rounds")
  public ModelAndView createRound(@PathVariable(name = "tournamentId") Long tournamentId,
                                  @Valid @ModelAttribute(name = "round") RoundToCreateView roundToCreate) {
    roundService.create(tournamentId, roundToCreate);
    return new ModelAndView("redirect:/admin/tournaments/" + tournamentId.toString() + "/rounds");
  }

  @GetMapping("/rounds/{roundId}")
  public ModelAndView getRound(@PathVariable(name = "roundId") Long roundId) {
    Round round = roundRepository.findOne(roundId);
    RoundListView roundDetail = convertListItem(round, true);
    List<PoolView> pools = poolRepository.findAllByRoundOrderByPosition(round).stream()
      .map(this::convert).collect(toList());
    Map<String, Object> model = Maps.newHashMap();
    model.put("tournament", round.getTournament());
    model.put("round", roundDetail);
    model.put("pools", pools);
    model.put("isProd", mainProperties.isProd());
    model.put("duplicatedTeams", hasSameTeams(round));
    model.put("started", round.isStarted());
    return new ModelAndView("round_detail", model);
  }

  @GetMapping("/rounds/{roundId}/matches")
  public ModelAndView validateRound(@PathVariable(name = "roundId") Long roundId) {
    Round round = roundRepository.findOne(roundId);
    if (round.getPools().get(0).getMatches().isEmpty()) {
      round.setStatus(RoundStatus.STARTED);
      matchGenerationService.generate(round);
      roundRepository.save(round);
    }
    return new ModelAndView("redirect:/admin/rounds/" + roundId + "/matches/papers");
  }

  @GetMapping("/rounds/{roundId}/finish")
  public ModelAndView finishRound(@PathVariable(name = "roundId") Long roundId) {
    Round round = roundRepository.findOne(roundId);
    round.setStatus(RoundStatus.FINISHED);
    roundRepository.save(round);
    return new ModelAndView("redirect:/admin/tournaments/" + round.getTournament().getId() + "/rounds");
  }

  @GetMapping("/rounds/{roundId}/matches/generate")
  public ModelAndView generateMatchResults(@PathVariable(name = "roundId") Long roundId) {
    generatorService.generateMatchResults(roundId);
    return new ModelAndView("redirect:/admin/rounds/" + roundId);
  }

  @PutMapping("/rounds/{roundId}/switch")
  public ResponseEntity switchTeams(@PathVariable(name = "roundId") Long roundId,
                                    @Valid @ModelAttribute SwitchTeamsFromPool body) {
    Pool pool1 = poolRepository.findOne(body.getPool1Id());
    Team team1 = teamRepository.findOne(body.getTeam1Id());
    Pool pool2 = poolRepository.findOne(body.getPool2Id());
    Team team2 = teamRepository.findOne(body.getTeam2Id());
    pool1.replace(team1, team2);
    pool2.replace(team2, team1);
    poolRepository.save(pool1);
    poolRepository.save(pool2);
    Map<String, String> result = Maps.newHashMap();
    result.put("redirect", "/admin/rounds/" + roundId);
    return ResponseEntity.ok(result);
  }

  RoundListView convertListItem(Round round, boolean full) {
    RoundListViewBuilder builder = RoundListViewBuilder.aRoundListView()
      .withId(round.getId())
      .withBranch(round.getBranch().getLabel())
      .withBranchColor(round.getBranch().getColor())
      .withName(round.getName())
      .withStatus(round.getStatus().getLabel())
      .withTypeLabel(round.getType().getLabel())
      .withTypeValue(String.valueOf(round.getPools().size()))
      .withFields(round.getFieldRanges())
      .withTypeLast(getTypeLast(round))
      .withTeams(getTeams(round))
      .withFieldsLast(getFieldsLast(round));
    if (full) {
      builder.withName(round.getFullName());
    }
    return builder.build();
  }

  @NotNull
  private String getTypeLast(Round round) {
    long count = round.getPools().parallelStream().filter(pool -> !pool.isFinished()).count();
    if (count == 0L) {
      return "Aucun";
    }
    return String.valueOf(count);
  }

  private String getTeams(Round round) {
    List<String> teams = round.getPreviousRounds().stream().map(previousRound -> previousRound.getTeamsFrom() + "-" + previousRound.getTeamsTo()).collect(Collectors.toList());
    if (teams.isEmpty()) {
      return "Toutes";
    }
    return String.join(";", teams);
  }

  private String getFieldsLast(Round round) {
    return "";
  }

  private PoolView convert(Pool pool) {
    List<TeamOverviewView> teams = Lists.newArrayList(
      getTeamOverview(pool.getTeam1(), "A", pool),
      getTeamOverview(pool.getTeam2(), "B", pool)
    );
    if (RoundType.POOL.equals(pool.getRound().getType())) {
      teams.add(getTeamOverview(pool.getTeam3(), "C", pool));
    }
    PoolViewBuilder poolViewBuilder = aPoolView()
      .withId(pool.getId())
      .withName(pool.getFullName())
      .withField(pool.getField())
      .withTeams(teams);
    if (pool.isFinished()) {
      poolViewBuilder.isFinished();
    }
    return poolViewBuilder
      .build();
  }

  TeamOverviewView getTeamOverview(Team team, String letter, Pool pool) {
    TeamOverviewViewBuilder builder = aTeamOverviewView()
      .withId(team.getId())
      .withLetter(letter)
      .withName(team.getName())
      .withLevel(team.getLevel())
      .withPlayedAlready(false);

    pool.getRound().getPreviousRounds().forEach(previousRound -> {
      Pool previousPool = poolRepository.findByRoundAndTeam(previousRound.getPreviousRound(), team);
      if (previousPool != null) {
        builder.withPreviousRank(previousPool.getRanking(team).getPosition());
        builder.withPreviousRankColor(previousRound.getPreviousRound().getBranch().getColor());
        Team team1 = pool.getTeam1();
        Team team2 = pool.getTeam2();
        if (team.equals(pool.getTeam1())) {
          team1 = pool.getTeam2();
          team2 = pool.getTeam3();
        }
        if (team.equals(pool.getTeam2())) {
          team2 = pool.getTeam3();
        }
        boolean hasAlreadyPlayedAgainst = teamService.hasAlreadyPlayedAgainst(pool.getRound(), team, team1, team2);
        builder.withPlayedAlready(hasAlreadyPlayedAgainst);
      }
    });
    return builder.build();
  }

  private boolean hasSameTeams(Round round) {
    List<Team> teams = Lists.newArrayList();
    for (Pool pool : round.getPools()) {
      if (teams.contains(pool.getTeam1()) || teams.contains(pool.getTeam2()) || teams.contains(pool.getTeam3())) {
        logger.error("In Round {}, Pool {} contains existing team", round.getId(), pool.getId());
        return true;
      }
      teams.add(pool.getTeam1());
      teams.add(pool.getTeam2());
      if (RoundType.POOL.equals(pool.getRound().getType())) {
        teams.add(pool.getTeam3());
      }
    }
    return false;
  }
}
