package org.fmarin.admintournoi.subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final TeamRepository repository;

    @Autowired
    public SubscriptionController(TeamRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/new")
    public ModelAndView index() {
        return new ModelAndView("subscription_form");
    }

    @PostMapping
    public ModelAndView subscribe(@ModelAttribute Team team) {
        repository.save(team);
        return new ModelAndView("subscription_form");
    }
}
