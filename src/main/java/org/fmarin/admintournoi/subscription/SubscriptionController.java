package org.fmarin.admintournoi.subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

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

    @GetMapping("/{id}/payment")
    public ModelAndView initPayment(@PathVariable("id")Long subscriptionId) {
        Map<String, Object> model = new HashMap<>();
        model.put("subscriptionId", subscriptionId);
        return new ModelAndView("subscription_payment", model);
    }

    @PostMapping
    public RedirectView subscribe(@ModelAttribute Subscription subscription) {
        Long teamId = service.subscribe(subscription);
        return new RedirectView("/subscriptions/" + teamId.toString()+ "/payment");
    }
}
