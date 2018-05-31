package org.fmarin.admintournoi;

import com.google.common.collect.Maps;
import org.fmarin.admintournoi.features.FeatureManager;
import org.fmarin.admintournoi.helper.TimeMachine;
import org.fmarin.admintournoi.subscription.Gender;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LayoutController {

  private final FeatureManager features;
  private final TournamentRepository tournamentRepository;

  @Autowired
  public LayoutController(FeatureManager features, TournamentRepository tournamentRepository) {
    this.features = features;
    this.tournamentRepository = tournamentRepository;
  }

  protected Map<String, Object> getBaseModel() {
    Map<String, Object> model = Maps.newHashMap();
    model.put("subscriptions_enabled", features.areSubscriptionsEnabled());
    Tournament womenTournament = tournamentRepository.findByYearAndGender(TimeMachine.now().getYear(), Gender.WOMEN);
    Tournament menTournament = tournamentRepository.findByYearAndGender(TimeMachine.now().getYear(), Gender.MEN);
    model.put("subscriptions_opened", menTournament.areSubscriptionsOpened());
    model.put("tournaments_full", womenTournament.isFull() && menTournament.isFull());
    model.put("event_started", getFeatures().isEventStarted());
    return model;
  }

  protected TournamentRepository getTournamentRepository() {
    return this.tournamentRepository;
  }

  protected FeatureManager getFeatures() {
    return this.features;
  }
}
