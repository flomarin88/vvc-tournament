package org.fmarin.admintournoi.admin;

import com.google.common.collect.Maps;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
public class AdminController {

    private final TournamentRepository tournamentRepository;

    @Autowired
    public AdminController(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @GetMapping("/admin")
    public ModelAndView index() {
        Tournament women = tournamentRepository.findOne(1L);
        Tournament men = tournamentRepository.findOne(2L);
        Map<String, Long> model = Maps.newHashMap();
        model.put("womenTeamsCount", getCount(women));
        model.put("menTeamsCount", getCount(men));
        return new ModelAndView("admin_index", model);
    }

    private long getCount(Tournament tournament) {
        return tournament.getTeams().parallelStream()
                .filter(item -> "Completed".equals(item.getPaymentStatus()))
                .count();
    }
}
