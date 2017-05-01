package org.fmarin.admintournoi.subscription;

import com.benfante.paypal.ipnassistant.IpnAssistant;
import com.benfante.paypal.ipnassistant.IpnData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    public ModelAndView index(Model model) {
        if (!model.containsAttribute("subscription")) {
            model.addAttribute("subscription", new Subscription());
        }
        return new ModelAndView("subscription_form");
    }

    @PostMapping("/new")
    public ModelAndView subscribe(@Valid @ModelAttribute("subscription") Subscription subscription, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("subscription_form");
            modelAndView.addAllObjects(model);
            return modelAndView;
        }
        else {
            Long teamId = service.subscribe(subscription);
            return new ModelAndView("redirect:/subscriptions/" + teamId.toString() + "/payment");
        }
    }

    @GetMapping("/{id}/payment")
    public ModelAndView initPayment(@PathVariable("id")Long subscriptionId) {
        Map<String, Object> model = new HashMap<>();
        model.put("subscriptionId", subscriptionId);
        return new ModelAndView("subscription_payment", model);
    }



    @PostMapping("/ipn")
    public ResponseEntity<?> handleIPNMessages(HttpServletRequest request) {
        IpnData ipnData = IpnData.buildFromHttpRequestParams(request.getParameterMap());
        ipnAssistant.process(ipnData);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
