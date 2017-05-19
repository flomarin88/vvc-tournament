package org.fmarin.admintournoi.admin.pool;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.fmarin.admintournoi.subscription.Team;
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
public class RoundController {

    private final TournamentRepository tournamentRepository;
    private final RoundRepository roundRepository;

    @Autowired
    public RoundController(TournamentRepository tournamentRepository, RoundRepository roundRepository) {
        this.tournamentRepository = tournamentRepository;
        this.roundRepository = roundRepository;
    }

    @GetMapping("/tournaments/{tournamentId}/rounds")
    public ModelAndView index(@PathVariable(name = "tournamentId") Long tournamentId) {
        Tournament tournament = tournamentRepository.findOne(tournamentId);
        List<RoundView> rounds = roundRepository.findAllByTournament(tournament).stream()
                .map(this::convert).collect(Collectors.toList());
        Map<String, Object> model = Maps.newHashMap();
        model.put("tournament", tournament);
        model.put("rounds", rounds);
        return new ModelAndView("round_index", model);
    }

    @GetMapping("/tournaments/{tournamentId}/rounds/new")
    public ModelAndView newRound(@Valid @PathVariable(name = "tournamentId") Long tournamentId) {
        Tournament tournament = tournamentRepository.findOne(tournamentId);
        return new ModelAndView("round_new", "tournament", tournament);
    }

    @GetMapping("/rounds/{roundId}/teams")
    public List<Team> getTeams(@Valid @PathVariable(name = "roundId") Long roundId) {
        return roundRepository.findOne(roundId).getTeams();
    }

    @PostMapping("/tournaments/{tournamentId}/rounds")
    public ModelAndView createRound(@PathVariable(name = "tournamentId") Long tournamentId, @Valid @ModelAttribute(name = "round") RoundToCreateView roundToCreate) {
        Tournament tournament = tournamentRepository.findOne(tournamentId);
        Round previousRound = null;
        List<Team> teams = Collections.emptyList();
        if (roundToCreate.getPreviousRoundId() == null) {
            teams = Lists.newArrayList(tournament.getTeams());
        }
        else {
            previousRound = roundRepository.findOne(roundToCreate.getPreviousRoundId());
        }
        Round round = RoundBuilder.aRound()
                .withName(roundToCreate.getName())
                .withBranch(TournamentBranch.valueOf(roundToCreate.getTournamentBranch()))
                .withStatus(RoundStatus.CREATED)
                .withPreviousRound(previousRound)
                .withTeams(teams)
                .withTournament(tournament)
                .build();
        roundRepository.save(round);
        return new ModelAndView("redirect:/tournaments/" + tournamentId.toString() + "/rounds");
    }

    RoundView convert(Round round) {
        String previousRoundLabel = round.getPreviousRound() != null ? round.getPreviousRound().getName() : "-";
        return RoundViewBuilder.aPoolView()
                .withId(round.getId())
                .withName(round.getName())
                .withPreviousRoundName(previousRoundLabel)
                .withTeamsCount(round.getTeams().size())
                .withStatus(round.getStatus().name())
                .build();
    }
}
