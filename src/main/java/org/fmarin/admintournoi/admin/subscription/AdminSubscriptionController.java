package org.fmarin.admintournoi.admin.subscription;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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

  @Autowired
  public AdminSubscriptionController(TournamentRepository tournamentRepository) {
    this.tournamentRepository = tournamentRepository;
  }

  @GetMapping("/tournaments/{tournamentId}/subscriptions")
  public ModelAndView index(@PathVariable(name = "tournamentId") Long id) {
    Tournament tournament = tournamentRepository.findOne(id);
    Map<String, Object> model = Maps.newHashMap();
    model.put("tournamentId", tournament.getId());
    model.put("tournamentName", tournament.getFullName());
    model.put("teams", tournament.getSubscribedTeams().stream().map(this::build).collect(Collectors.toList()));
    return new ModelAndView("/admin/subscriptions", model);
  }

  private AdminSubscribedTeamView build(Team team) {
    return new AdminSubscribedTeamView(
      team.getName(),
      team.getLevel().getLabel(),
      buildPlayers(team),
      team.getPaymentVerificationCode()
    );
  }

  private List<PlayerView> buildPlayers(Team team) {
    PlayerView captain = new PlayerView(team.getCaptainName(), team.getCaptainClub(), team.getCaptainEmail(), team.getCaptainEmail());
    PlayerView player2 = new PlayerView(team.getPlayer2Name(), team.getPlayer2Club(), team.getPlayer2Email(), "");
    PlayerView player3 = new PlayerView(team.getPlayer3Name(), team.getPlayer3Club(), team.getPlayer3Email(), "");
    return Lists.newArrayList(captain, player2, player3);
  }
}
