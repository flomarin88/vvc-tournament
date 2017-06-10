package org.fmarin.admintournoi.admin.round;

import com.google.common.collect.Maps;
import org.fmarin.admintournoi.admin.match.MatchGenerationService;
import org.fmarin.admintournoi.admin.pool.Pool;
import org.fmarin.admintournoi.admin.pool.PoolRepository;
import org.fmarin.admintournoi.admin.pool.PoolView;
import org.fmarin.admintournoi.admin.pool.PoolViewBuilder;
import org.fmarin.admintournoi.admin.ranking.RankingService;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.TeamRepository;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class RoundController {

    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final RoundRepository roundRepository;
    private final PoolRepository poolRepository;
    private final MatchGenerationService matchGenerationService;
    private final RoundService roundService;
    private final RankingService rankingService;

    @Autowired
    public RoundController(TournamentRepository tournamentRepository, TeamRepository teamRepository,
                           RoundRepository roundRepository, PoolRepository poolRepository,
                           MatchGenerationService matchGenerationService, RoundService roundService,
                           RankingService rankingService) {
        this.tournamentRepository = tournamentRepository;
        this.teamRepository = teamRepository;
        this.roundRepository = roundRepository;
        this.poolRepository = poolRepository;
        this.matchGenerationService = matchGenerationService;
        this.roundService = roundService;
        this.rankingService = rankingService;
    }

    @GetMapping("/tournaments/{tournamentId}/rounds")
    public ModelAndView index(@PathVariable(name = "tournamentId") Long tournamentId) {
        Tournament tournament = tournamentRepository.findOne(tournamentId);
        List<RoundListView> rounds = roundRepository.findAllByTournament(tournament).stream()
                .map(this::convertListItem).collect(Collectors.toList());
        Map<String, Object> model = Maps.newHashMap();
        model.put("tournament", tournament);
        model.put("rounds", rounds);
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
        RoundListView roundDetail = convertListItem(round);
        List<PoolView> pools = poolRepository.findAllByRoundOrderByPosition(round).stream()
                .map(this::convert).collect(Collectors.toList());
        Map<String, Object> model = Maps.newHashMap();
        model.put("round", roundDetail);
        model.put("pools", pools);
        return new ModelAndView("round_detail", model);
    }

    @GetMapping("/rounds/{roundId}/matches")
    public ModelAndView validateRound(@PathVariable(name = "roundId") Long roundId) {
        Round round = roundRepository.findOne(roundId);
        round.setStatus(RoundStatus.STARTED);
        matchGenerationService.generate(round);
        roundRepository.save(round);
        return new ModelAndView("redirect:/admin/rounds/" + roundId + "/matches/papers");
    }

    @PutMapping("/rounds/{roundId}/switch")
    public ResponseEntity switchTeams(@PathVariable(name = "roundId") Long roundId,
                                      @Valid @ModelAttribute SwitchTeamsFromPool body) {
        Pool pool1 = poolRepository.findOne(body.getPool1Id());
        Team team1 = teamRepository.findOne(body.getTeam1Id());
        Pool pool2 = poolRepository.findOne(body.getPool2Id());
        Team team2 = teamRepository.findOne(body.getTeam2Id());
        switchTeams(pool1, team1, team2);
        switchTeams(pool2, team2, team1);
        poolRepository.save(pool1);
        poolRepository.save(pool2);
        Map<String, String> result = Maps.newHashMap();
        result.put("redirect", "/admin/rounds/" + roundId);
        return ResponseEntity.ok(result);
    }

    void switchTeams(Pool pool, Team team1, Team team2) {
        if (team1.equals(pool.getTeam1())) {
            pool.setTeam1(team2);
        }
        if (team1.equals(pool.getTeam2())) {
            pool.setTeam2(team2);
        }
        if (team1.equals(pool.getTeam3())) {
            pool.setTeam3(team2);
        }
    }

    RoundListView convertListItem(Round round) {
        String previousRoundLabel = round.getPreviousRound() != null ? round.getPreviousRound().getName() : "-";
        return RoundListViewBuilder.aRoundListView()
                .withId(round.getId())
                .withName(round.getBranch().getLabel() + " - " + round.getName())
                .withType(round.getType().getLabel())
                .withPreviousRoundName(previousRoundLabel)
                .withTeamsCount(round.getTeams().size())
                .withStatus(round.getStatus().getLabel())
                .withTournamentId(round.getTournament().getId())
                .withTournamentName(round.getTournament().getName())
                .withValidated(round.getStatus().equals(RoundStatus.STARTED))
                .build();
    }

    private PoolView convert(Pool pool) {
        PoolViewBuilder poolView = PoolViewBuilder.aPoolView()
                .withId(pool.getId())
                .withName("Poule " + pool.getPosition())
                .withField(pool.getField())
                .withTeamId1(pool.getTeam1().getId())
                .withTeamId2(pool.getTeam2().getId())
                .withTeamId3(pool.getTeam3().getId())
                .withTeamName1(pool.getTeam1().getName())
                .withTeamName2(pool.getTeam2().getName())
                .withTeamName3(pool.getTeam3().getName())
                .withColor(getColorStatus(pool));

        if (pool.getRound().getPreviousRound() != null) {
            poolView.withTeamPreviousRank1(rankingService.getTeamRanking(pool.getTeam1(),
                    pool.getRound().getPreviousRound()).getPosition());
            poolView.withTeamPreviousRank2(rankingService.getTeamRanking(pool.getTeam2(),
                    pool.getRound().getPreviousRound()).getPosition());
            poolView.withTeamPreviousRank3(rankingService.getTeamRanking(pool.getTeam3(),
                    pool.getRound().getPreviousRound()).getPosition());
        }
        return poolView
                .withTeamLevel1(pool.getTeam1().getLevel().getLabel())
                .withTeamLevel2(pool.getTeam2().getLevel().getLabel())
                .withTeamLevel3(pool.getTeam3().getLevel().getLabel())
                .withTeamLevelColor1("label-" + pool.getTeam1().getLevel().getColor())
                .withTeamLevelColor2("label-" + pool.getTeam2().getLevel().getColor())
                .withTeamLevelColor3("label-" + pool.getTeam3().getLevel().getColor())
                .build();
    }

    String getColorStatus(Pool pool) {
        long count = pool.getMatches().parallelStream().filter(match -> !match.isFinished()).count();
        return pool.getMatches().size() == 0 || count > 0 ? "primary" : "success";
    }

}
