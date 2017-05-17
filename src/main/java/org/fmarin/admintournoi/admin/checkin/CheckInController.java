package org.fmarin.admintournoi.admin.checkin;

import com.google.common.collect.ImmutableMap;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.TeamRepository;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/tournament/{id}/checkin")
public class CheckInController {

    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;

    @Autowired
    public CheckInController(TeamRepository teamRepository, TournamentRepository tournamentRepository) {
        this.teamRepository = teamRepository;
        this.tournamentRepository = tournamentRepository;
    }

    @GetMapping
    public ModelAndView index(@PathVariable(name = "id") Long id) {
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
