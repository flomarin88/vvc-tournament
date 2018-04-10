package org.fmarin.admintournoi;

import com.google.common.collect.Maps;
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
public class LandingController {

  private final FeatureManager features;
  private final TournamentRepository tournamentRepository;

  @Autowired
  public LandingController(FeatureManager features, TournamentRepository tournamentRepository) {
    this.features = features;
    this.tournamentRepository = tournamentRepository;
  }

  @GetMapping("/")
  public ModelAndView index() {
    Map<String, Object> model = Maps.newHashMap();
    model.put("subscriptions_enabled", features.areSubscriptionsEnabled());
    Tournament womenTournament = tournamentRepository.findByYearAndGender(TimeMachine.now().getYear(), Gender.WOMEN);
    Tournament menTournament = tournamentRepository.findByYearAndGender(TimeMachine.now().getYear(), Gender.MEN);
    model.put("subscriptions_opened", menTournament.areSubscriptionsOpened());
    model.put("tournaments_full", womenTournament.isFull() && menTournament.isFull());

    return new ModelAndView("landing", model);
  }

}
