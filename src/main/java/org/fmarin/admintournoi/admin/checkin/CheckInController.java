package org.fmarin.admintournoi.admin.checkin;

import com.google.common.collect.ImmutableMap;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.TeamRepository;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class CheckInController {

    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;

    private static final Logger logger = LoggerFactory.getLogger(CheckInController.class);

    @Autowired
    public CheckInController(TeamRepository teamRepository, TournamentRepository tournamentRepository) {
        this.teamRepository = teamRepository;
        this.tournamentRepository = tournamentRepository;
    }

    @GetMapping(("/admin/tournament/{tournamentId}/checkin"))
    public ModelAndView index(@PathVariable(name = "tournamentId") Long id) {
        Tournament tournament = tournamentRepository.findOne(id);
        List<Team> teams = teamRepository.findAllByTournamentAndPaymentStatusOrderByNameAsc(tournament, "Completed");
        List<TeamToCheckInView> teamsToCheckin = teams.stream().map(this::convert).collect(Collectors.toList());
        Map<String, Object> model = new ImmutableMap.Builder<String, Object>()
                .put("tournament", tournament)
                .put("teamsToCheckinCount", teams.size())
                .put("teams", teamsToCheckin)
                .build();
        return new ModelAndView("teams", model);
    }

    @PostMapping("/admin/teams/{teamId}/checkin")
    public ResponseEntity checkIn(@PathVariable(name = "teamId") Long teamId, @ModelAttribute(name = "isPresent") boolean isPresent) {
        logger.info("Team {} is {}", teamId.toString(), isPresent ? "PRESENT" : "ABSENT");
        Team team = teamRepository.findOne(teamId);
        team.setPresent(isPresent);
        teamRepository.save(team);
        return ResponseEntity.ok().build();
    }

    TeamToCheckInView convert(Team team) {
        return new TeamToCheckInView(
                team.getId(),
                team.getName(),
                team.getTournament().getName(),
                team.getLevel().getLabel(),
                team.getCaptainName(),
                team.getCaptainEmail(),
                team.getCaptainPhone(),
                team.getPaymentVerificationCode(),
                team.isPresent());
    }

}
