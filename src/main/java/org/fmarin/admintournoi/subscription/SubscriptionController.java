package org.fmarin.admintournoi.subscription;

import com.benfante.paypal.ipnassistant.IpnAssistant;
import com.benfante.paypal.ipnassistant.IpnData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService service;
    private final IpnAssistant ipnAssistant;

    @Autowired
    public SubscriptionController(SubscriptionService service, IpnAssistant ipnAssistant) {
        this.service = service;
        this.ipnAssistant = ipnAssistant;
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

    @PostMapping("/ipn")
    public ResponseEntity<?> handleIPNMessages(HttpServletRequest request) {
        IpnData ipnData = IpnData.buildFromHttpRequestParams(request.getParameterMap());
        ipnAssistant.process(ipnData);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
