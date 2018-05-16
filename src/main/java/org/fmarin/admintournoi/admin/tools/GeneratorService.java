package org.fmarin.admintournoi.admin.tools;

import org.fmarin.admintournoi.admin.match.Match;
import org.fmarin.admintournoi.admin.pool.Pool;
import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.admin.round.RoundRepository;
import org.fmarin.admintournoi.subscription.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class GeneratorService {

  private final TournamentRepository tournamentRepository;
  private final TeamRepository teamRepository;
  private final RoundRepository roundRepository;

  @Autowired
  public GeneratorService(TournamentRepository tournamentRepository, TeamRepository teamRepository, RoundRepository roundRepository) {
    this.tournamentRepository = tournamentRepository;
    this.teamRepository = teamRepository;
    this.roundRepository = roundRepository;
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
        .withPaymentStatus("Completed")
        .withCreatedAt(LocalDateTime.now())
        .build();
      teamRepository.save(team);
      i++;
    }
    return true;
  }

  public boolean generateMatchResults(Long roundId) {
    Round round = roundRepository.findOne(roundId);
    for (Pool pool : round.getPools()) {
      for (Match match : pool.getMatches()) {
        int winner = randomWinner();
        if (winner == 1) {
          match.setScoreTeam1(25);
          match.setScoreTeam2(randomScore());
        }
        else {
          match.setScoreTeam1(randomScore());
          match.setScoreTeam2(25);
        }
      }
    }
    roundRepository.save(round);
    return true;
  }

  private Level randomLevel() {
    Random r = new Random();
    int random = r.ints(0, 4).findFirst().getAsInt();
    return Level.valueOf(random + 1);
  }

  private int randomWinner() {
    Random r = new Random();
    return r.ints(0, 2).findFirst().getAsInt() + 1;
  }

  private int randomScore() {
    Random r = new Random();
    return r.ints(0, 24).findFirst().getAsInt();
  }
}
