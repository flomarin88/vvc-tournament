package org.fmarin.admintournoi;

import com.google.common.collect.Maps;
import org.fmarin.admintournoi.features.FeatureManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
public class LandingController {

  private final FeatureManager features;

  @Autowired
  public LandingController(FeatureManager features) {
    this.features = features;
  }

  @GetMapping("/")
  public ModelAndView index() {
    Map<String, Object> model = Maps.newHashMap();
    model.put("subscriptions_enabled", features.areSubscriptionsEnabled());
    model.put("subscriptions_opened", features.areSubscriptionsOpened());
    return new ModelAndView("landing", model);
  }

}
