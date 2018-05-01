package org.fmarin.admintournoi.admin.subscription;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.fmarin.admintournoi.MainProperties;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminSubscriptionController {

  private final TournamentRepository tournamentRepository;
  private final MainProperties mainProperties;

  @Autowired
  public AdminSubscriptionController(TournamentRepository tournamentRepository, MainProperties mainProperties) {
    this.tournamentRepository = tournamentRepository;
    this.mainProperties = mainProperties;
  }

  @GetMapping("/tournaments/{tournamentId}/subscriptions")
  public ModelAndView index(@PathVariable(name = "tournamentId") Long id) {
    Tournament tournament = tournamentRepository.findOne(id);
    Map<String, Object> model = Maps.newHashMap();
    model.put("tournamentId", tournament.getId());
    model.put("tournamentName", tournament.getFullName());
    model.put("teams", tournament.getSubscribedTeams().stream().map(this::build).collect(Collectors.toList()));
    model.put("staging", !mainProperties.isProd());
    return new ModelAndView("admin/subscriptions", model);
  }

  private AdminSubscribedTeamView build(Team team) {
    return new AdminSubscribedTeamView(
      team.getName(),
      team.getLevel(),
      buildPlayers(team),
      team.getPaymentVerificationCode()
    );
  }

  private List<PlayerView> buildPlayers(Team team) {
    PlayerView captain = new PlayerView(team.getCaptainName(), Strings.nullToEmpty(team.getCaptainClub()), Strings.nullToEmpty(team.getCaptainEmail()), Strings.nullToEmpty(team.getCaptainPhone()));
    PlayerView player2 = new PlayerView(team.getPlayer2Name(), Strings.nullToEmpty(team.getPlayer2Club()), Strings.nullToEmpty(team.getPlayer2Email()), "");
    PlayerView player3 = new PlayerView(team.getPlayer3Name(), Strings.nullToEmpty(team.getPlayer3Club()), Strings.nullToEmpty(team.getPlayer3Email()), "");
    return Lists.newArrayList(captain, player2, player3);
  }
}
