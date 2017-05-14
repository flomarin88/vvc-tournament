package org.fmarin.admintournoi.admin.checkin;

import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/checkin")
public class CheckInController {

    private final TeamRepository teamRepository;

    @Autowired
    public CheckInController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @GetMapping
    public ModelAndView index() {
        List<Team> teams = teamRepository.findAllByPaymentStatusOrderByNameAsc("Completed");
        List<TeamToCheckInView> teamsToCheckin = teams.stream().map(this::convert).collect(Collectors.toList());
        return new ModelAndView("teams", "teams", teamsToCheckin);
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
                team.getPaymentVerficationCode(),
                false);
    }

}
