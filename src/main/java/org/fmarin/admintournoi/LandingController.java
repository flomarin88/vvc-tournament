package org.fmarin.admintournoi;

import org.fmarin.admintournoi.features.FeatureManager;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LandingController extends LayoutController {

  @Autowired
  public LandingController(FeatureManager features, TournamentRepository tournamentRepository) {
    super(features, tournamentRepository);
  }

  @GetMapping("/")
  public ModelAndView index() {
    return new ModelAndView("landing", getBaseModel());
  }

}
