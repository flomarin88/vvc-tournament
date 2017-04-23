package org.fmarin.admintournoi.subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService service;

    @Autowired
    public SubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    @GetMapping("/new")
    public ModelAndView index() {
        return new ModelAndView("subscription_form");
    }

    @PostMapping
    public ModelAndView subscribe(@ModelAttribute Subscription subscription) {
        boolean result = service.subscribe(subscription);
        return null;
    }
}
