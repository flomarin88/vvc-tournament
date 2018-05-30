package org.fmarin.admintournoi.subscription;

import com.benfante.paypal.ipnassistant.IpnAssistant;
import com.benfante.paypal.ipnassistant.IpnData;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import org.fmarin.admintournoi.LayoutController;
import org.fmarin.admintournoi.MainProperties;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.fmarin.admintournoi.subscription.SubscribedTeamViewBuilder.*;
import static org.fmarin.admintournoi.subscription.TeamsByTournamentViewBuilder.*;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController extends LayoutController {

  private final SubscriptionService service;
  private final IpnAssistant ipnAssistant;
  private final MainProperties mainProperties;

  @Autowired
  public SubscriptionController(SubscriptionService service, IpnAssistant ipnAssistant, TournamentRepository tournamentRepository, FeatureManager features, MainProperties mainProperties) {
    super(features, tournamentRepository);
    this.service = service;
    this.ipnAssistant = ipnAssistant;
    this.mainProperties = mainProperties;
  }

  @GetMapping
  public ModelAndView index() {
    Tournament womenTournament = getTournamentRepository().findByYearAndGender(TimeMachine.now().getYear(), Gender.WOMEN);
    Tournament menTournament = getTournamentRepository().findByYearAndGender(TimeMachine.now().getYear(), Gender.MEN);
    Map<String, Object> model = getBaseModel();
    model.put("tournaments", Lists.newArrayList(build(womenTournament), build(menTournament)));
    return new ModelAndView("public/teams", model);
  }

  @GetMapping("/new")
  public ModelAndView newSubscription(Model model) {
    Tournament womenTournament = getTournamentRepository().findByYearAndGender(TimeMachine.now().getYear(), Gender.WOMEN);
    Tournament menTournament = getTournamentRepository().findByYearAndGender(TimeMachine.now().getYear(), Gender.MEN);
    if (getFeatures().areSubscriptionsEnabled() && womenTournament.areSubscriptionsOpened() && (!womenTournament.isFull() || !menTournament.isFull())) {
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
        Map<String, Object> model = getBaseModel();
        Team team = service.subscribe(subscription);
        model.put("team", team);
        model.put("tournamentLabel", team.getTournament().getFullName());
        model.put("levelLabel", team.getLevel().getLabel());
        model.put("paypal_id", team.getTournament().getPaypalButtonId());
        String paypalUrl = "https://www.sandbox.paypal.com/cgi-bin/webscr";
        if (mainProperties.isProd()) {
          paypalUrl = "https://www.paypal.com/cgi-bin/webscr";
        }
        model.put("paypal_url", paypalUrl);
        return new ModelAndView("subscription_payment", model);
      } catch (TournamentIsFullException exception) {
        return new ModelAndView("redirect:/subscriptions/full", getBaseModel());
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
    ModelAndView modelAndView = new ModelAndView("subscription_form", getBaseModel());
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

  private TeamsByTournamentView build(Tournament tournament) {
    List<SubscribedTeamView> teams = Streams.mapWithIndex(
      tournament.getSubscribedTeams().stream()
        .sorted(Comparator.comparing(Team::getCreatedAt)),
      (team, index) -> build(index + 1, team))
      .collect(Collectors.toList());
    return aView()
      .withName(tournament.getFullName())
      .withTeams(teams)
      .build();
  }

  private SubscribedTeamView build(Long index, Team team) {
    return aTeam()
      .withIndex(index.intValue())
      .withName(team.getName())
      .build();
  }
}
