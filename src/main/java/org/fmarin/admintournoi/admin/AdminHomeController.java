package org.fmarin.admintournoi.admin;

import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/admin")
public class AdminHomeController {

    private final TeamRepository teamRepository;

    @Autowired
    public AdminHomeController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @GetMapping
    public ModelAndView index() {
        Iterable<Team> teams = teamRepository.findAll();
        return new ModelAndView("teams", "teams", teams);
    }

}
