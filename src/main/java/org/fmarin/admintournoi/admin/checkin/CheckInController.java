package org.fmarin.admintournoi.admin.checkin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.TeamRepository;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class CheckInController {

  private final TeamRepository teamRepository;
  private final TournamentRepository tournamentRepository;

  private static final Logger logger = LoggerFactory.getLogger(CheckInController.class);

  @Autowired
  public CheckInController(TeamRepository teamRepository, TournamentRepository tournamentRepository) {
    this.teamRepository = teamRepository;
    this.tournamentRepository = tournamentRepository;
  }

  @GetMapping(("/tournaments/{tournamentId}/checkin"))
  public ModelAndView index(@PathVariable(name = "tournamentId") Long id) {
    Tournament tournament = tournamentRepository.findOne(id);
    List<TeamToCheckInView> teamsToCheckin = tournament.getSubscribedTeams().stream()
      .map(this::convert)
      .collect(Collectors.toList());
    teamsToCheckin.sort(Comparator.comparing(TeamToCheckInView::getName));
    long absenceCount = teamsToCheckin.parallelStream().filter(team -> !team.isPresent()).count();
    Map<String, Object> model = new ImmutableMap.Builder<String, Object>()
      .put("tournament", tournament)
      .put("absenceCount", absenceCount)
      .put("teams", teamsToCheckin)
      .build();
    return new ModelAndView("admin/checkin", model);
  }

  @PostMapping("/teams/{teamId}/checkin")
  public ResponseEntity checkIn(@PathVariable(name = "teamId") Long teamId, @ModelAttribute(name = "isPresent") boolean isPresent) {
    logger.info("Team {} is {}", teamId.toString(), isPresent ? "PRESENT" : "ABSENT");
    Team team = teamRepository.findOne(teamId);
    team.setPresent(isPresent);
    teamRepository.save(team);
    Map<String, String> result = Maps.newHashMap();
    result.put("result", "ok");
    return ResponseEntity.ok(result);
  }

  TeamToCheckInView convert(Team team) {
    return new TeamToCheckInView(
      team.getId(),
      team.getName(),
      team.getLevel().getLabel(),
      team.getCaptainName(),
      team.getCaptainEmail(),
      team.getCaptainPhone(),
      team.getPaymentVerificationCode(),
      "A PAYER".equals(team.getCaptainName()),
      team.isPresent());
  }

}
