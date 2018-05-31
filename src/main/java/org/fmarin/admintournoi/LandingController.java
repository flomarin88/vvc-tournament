package org.fmarin.admintournoi;

import org.fmarin.admintournoi.features.FeatureManager;
import org.fmarin.admintournoi.helper.TimeMachine;
import org.fmarin.admintournoi.subscription.Gender;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
public class LandingController extends LayoutController {

  @Autowired
  public LandingController(FeatureManager features, TournamentRepository tournamentRepository) {
    super(features, tournamentRepository);
  }

  @GetMapping("/")
  public ModelAndView index() {
    Map<String, Object> model = getBaseModel();
    Tournament womenTournament = getTournamentRepository().findByYearAndGender(TimeMachine.now().getYear(), Gender.WOMEN);
    Tournament menTournament = getTournamentRepository().findByYearAndGender(TimeMachine.now().getYear(), Gender.MEN);
    model.put("women_subscriptions_left_count", getSubscriptionLeftCount(womenTournament));
    model.put("men_subscriptions_left_count", getSubscriptionLeftCount(menTournament));
    model.put("women_tournament_id", womenTournament.getId());
    model.put("men_tournament_id", menTournament.getId());
    return new ModelAndView("landing", model);
  }

  private int getSubscriptionLeftCount(Tournament tournament) {
    return tournament.getTeamLimit() - tournament.getSubscribedTeams().size();
  }
}
