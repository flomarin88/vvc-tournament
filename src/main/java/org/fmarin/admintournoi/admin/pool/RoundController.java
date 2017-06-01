package org.fmarin.admintournoi.admin.pool;

import com.google.common.collect.Maps;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.TeamRepository;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collections;
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
    private final PoolGenerationService poolGenerationService;

    @Autowired
    public RoundController(TournamentRepository tournamentRepository, TeamRepository teamRepository, RoundRepository roundRepository, PoolRepository poolRepository, PoolGenerationService poolGenerationService) {
        this.tournamentRepository = tournamentRepository;
        this.teamRepository = teamRepository;
        this.roundRepository = roundRepository;
        this.poolRepository = poolRepository;
        this.poolGenerationService = poolGenerationService;
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

    @GetMapping("/rounds/{roundId}/teams")
    public List<Team> getTeams(@Valid @PathVariable(name = "roundId") Long roundId) {
        return roundRepository.findOne(roundId).getTeams();
    }

    @PostMapping("/tournaments/{tournamentId}/rounds")
    public ModelAndView createRound(@PathVariable(name = "tournamentId") Long tournamentId,
                                    @Valid @ModelAttribute(name = "round") RoundToCreateView roundToCreate) {
        Tournament tournament = tournamentRepository.findOne(tournamentId);
        Round previousRound = null;
        List<Team> teams = Collections.emptyList();
        if (roundToCreate.getPreviousRoundId() == null) {
            teams = teamRepository.findAllByTournamentAndPaymentStatusOrderByNameAsc(tournament, "Completed");
        } else {
            previousRound = roundRepository.findOne(roundToCreate.getPreviousRoundId());
        }
        Round round = RoundBuilder.aRound()
                .withName(roundToCreate.getName())
                .withBranch(TournamentBranch.valueOf(roundToCreate.getTournamentBranch()))
                .withType(RoundType.valueOf(roundToCreate.getType()))
                .withStatus(RoundStatus.CREATED)
                .withPreviousRound(previousRound)
                .withTeams(teams)
                .withTournament(tournament)
                .build();
        roundRepository.save(round);
        poolGenerationService.generatePools(round);
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

    RoundListView convertListItem(Round round) {
        String previousRoundLabel = round.getPreviousRound() != null ? round.getPreviousRound().getName() : "-";
        return RoundListViewBuilder.aRoundListView()
                .withId(round.getId())
                .withName(round.getName())
                .withType(round.getType().getLabel())
                .withPreviousRoundName(previousRoundLabel)
                .withTeamsCount(round.getTeams().size())
                .withStatus(round.getStatus().getLabel())
                .withTournamentName(round.getTournament().getName())
                .build();
    }

    PoolView convert(Pool pool) {
        return PoolViewBuilder.aPoolView()
                .withId(pool.getId())
                .withName("Poule " + pool.getPosition())
                .withTeamId1(pool.getTeam1().getId())
                .withTeamId2(pool.getTeam2().getId())
                .withTeamId3(pool.getTeam3().getId())
                .withTeamName1(pool.getTeam1().getName())
                .withTeamName2(pool.getTeam2().getName())
                .withTeamName3(pool.getTeam3().getName())
                .withTeamLevel1(pool.getTeam1().getLevel().getLabel())
                .withTeamLevel2(pool.getTeam2().getLevel().getLabel())
                .withTeamLevel3(pool.getTeam3().getLevel().getLabel())
                .withTeamLevelColor1("label-" + pool.getTeam1().getLevel().getColor())
                .withTeamLevelColor2("label-" + pool.getTeam2().getLevel().getColor())
                .withTeamLevelColor3("label-" + pool.getTeam3().getLevel().getColor())
                .build();
    }

}
