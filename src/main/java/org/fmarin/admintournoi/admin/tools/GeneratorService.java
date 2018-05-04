package org.fmarin.admintournoi.admin.tools;

import org.fmarin.admintournoi.payment.PaymentStatus;
import org.fmarin.admintournoi.subscription.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GeneratorService {

  private final TournamentRepository tournamentRepository;
  private final TeamRepository teamRepository;

  @Autowired
  public GeneratorService(TournamentRepository tournamentRepository, TeamRepository teamRepository) {
    this.tournamentRepository = tournamentRepository;
    this.teamRepository = teamRepository;
  }

  public boolean generateTeams(Long tournamentId) {
    Tournament tournament = tournamentRepository.findOne(tournamentId);
    int i = tournament.getSubscribedTeams().size();
    while (i < tournament.getTeamLimit()) {
      Team team = TeamBuilder.aTeam()
        .withTournament(tournament)
        .withName("Team " + i)
        .withCaptainName("Captain " + i)
        .withLevel(randomLevel())
        .withCaptainPhone("0143242551")
        .withCaptainEmail("captain" + i + "@test.com")
        .withPlayer2Name("Player2 " + i)
        .withPlayer2Email("player2" + i + "@test.com")
        .withPlayer3Name("Player3 " + i)
        .withPlayer3Email("player3" + i + "@test.com")
        .withPaymentVerificationCode(i)
        .withPaymentStatus(PaymentStatus.COMPLETED)
        .build();
      teamRepository.save(team);
      i++;
    }
    return true;
  }

  private Level randomLevel() {
    Random r = new Random();
    int random = r.ints(0, 4).findFirst().getAsInt();
    return Level.valueOf(random + 1);
  }
}
