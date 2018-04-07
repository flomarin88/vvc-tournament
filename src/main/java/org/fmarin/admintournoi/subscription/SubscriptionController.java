package org.fmarin.admintournoi.subscription;

import com.benfante.paypal.ipnassistant.IpnAssistant;
import com.benfante.paypal.ipnassistant.IpnData;
import org.fmarin.admintournoi.features.FeatureManager;
import org.fmarin.admintournoi.helper.TimeMachine;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

  private final SubscriptionService service;
  private final IpnAssistant ipnAssistant;
  private final TournamentRepository tournamentRepository;
  private final FeatureManager features;

  @Autowired
  public SubscriptionController(SubscriptionService service, IpnAssistant ipnAssistant, SubscriptionProperties properties, TournamentRepository tournamentRepository, FeatureManager features) {
    this.service = service;
    this.ipnAssistant = ipnAssistant;
    this.tournamentRepository = tournamentRepository;
    this.features = features;
  }

  @GetMapping("/new")
  public ModelAndView index(Model model) {
    Tournament womenTournament = tournamentRepository.findByYearAndGender(TimeMachine.now().getYear(), Gender.WOMEN);
    Tournament menTournament = tournamentRepository.findByYearAndGender(TimeMachine.now().getYear(), Gender.MEN);
    if (features.areSubscriptionsEnabled() && features.areSubscriptionsOpened() && womenTournament.isFull() && menTournament.isFull()) {
      if (!model.containsAttribute("subscription")) {
        model.addAttribute("subscription", new Subscription());
      }
      return new ModelAndView("subscription_form");
    }
    return new ModelAndView("redirect:/#subscriptions");
  }

  @PostMapping("/new")
  public ModelAndView subscribe(@Valid @ModelAttribute("subscription") Subscription subscription, BindingResult result) {
    if (result.hasErrors()) {
      return getModelAndView(subscription, result);
    } else {
      try {
        Team team = service.subscribe(subscription);
        ModelAndView modelAndView = new ModelAndView("subscription_payment", "team", team);
        modelAndView.addObject("tournamentLabel", team.getTournament().getName());
        modelAndView.addObject("levelLabel", team.getLevel().getLabel());
        modelAndView.addObject("paypal_id", team.getTournament().getPaypalButtonId());
        return modelAndView;
      } catch (TournamentIsFullException exception) {
        return new ModelAndView("redirect:/subscriptions/full");
      } catch (TeamAlreadyExistsException exception) {
        ModelAndView modelAndView = getModelAndView(subscription, result);
        Map<String, Boolean> errors = (Map<String, Boolean>) modelAndView.getModel().get("errors");
        errors.put("teamExists", true);
        return modelAndView;
      }
    }
  }

  @GetMapping("/full")
  public ModelAndView tournamentFull() {
    return new ModelAndView("tournament_full");
  }

  @PostMapping("/ipn")
  public ResponseEntity<?> handleIPNMessages(HttpServletRequest request) {
    IpnData ipnData = IpnData.buildFromHttpRequestParams(request.getParameterMap());
    ipnAssistant.process(ipnData);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @NotNull
  private ModelAndView getModelAndView(@Valid @ModelAttribute("subscription") Subscription subscription, BindingResult result) {
    ModelAndView modelAndView = new ModelAndView("subscription_form");
    modelAndView.addObject("subscription", subscription);
    Map<String, Boolean> failingFields = result.getFieldErrors().stream().map(FieldError::getField).collect(Collectors.toMap(key -> key, value -> true));
    modelAndView.addObject("errors", failingFields);
    modelAndView.addObject(buildRadioMustache("level", subscription.getLevel()), true);
    modelAndView.addObject(buildRadioMustache("tournamentId", subscription.getTournamentId()), true);
    return modelAndView;
  }

  private String buildRadioMustache(String key, Number value) {
    return key + value.toString();
  }

}
