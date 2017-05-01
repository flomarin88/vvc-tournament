package org.fmarin.admintournoi;

import org.fmarin.admintournoi.subscription.SubscriptionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LandingController {

    private final SubscriptionProperties properties;

    @Autowired
    public LandingController(SubscriptionProperties properties) {
        this.properties = properties;
    }

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("landing", "subscriptionsEnable", properties.isEnable());
    }
    
}
