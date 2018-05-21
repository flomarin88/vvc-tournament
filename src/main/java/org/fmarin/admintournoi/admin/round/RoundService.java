package org.fmarin.admintournoi.admin.round;

import org.fmarin.admintournoi.admin.pool.PoolGenerationService;
import org.fmarin.admintournoi.admin.ranking.Ranking;
import org.fmarin.admintournoi.admin.ranking.RankingService;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoundService {

  private final TournamentRepository tournamentRepository;
  private final RoundRepository roundRepository;
  private final PoolGenerationService poolGenerationService;
  private final RankingService rankingService;

  @Autowired
  public RoundService(TournamentRepository tournamentRepository,
                      RoundRepository roundRepository, PoolGenerationService poolGenerationService,
                      RankingService rankingService) {
    this.tournamentRepository = tournamentRepository;
    this.roundRepository = roundRepository;
    this.poolGenerationService = poolGenerationService;
    this.rankingService = rankingService;
  }

  public void create(Long tournamentId, RoundToCreateView roundToCreate) {
    Tournament tournament = tournamentRepository.findOne(tournamentId);
    if (roundToCreate.getPreviousRounds().isEmpty()) {
      Round round = convert(roundToCreate, tournament, tournament.getSubscribedTeams(), null);
      roundRepository.save(round);
      poolGenerationService.generatePoolsWithLevels(round);
    } else {
      List<Ranking> rankings = rankingService.getRoundRanking(roundToCreate.getPreviousRounds().get(0).getRoundId());
      Round round = createNextRound(tournament, roundToCreate, rankings);
      roundRepository.save(round);
      poolGenerationService.generatePoolsWithRankings(round);
    }
  }

  private Round createNextRound(Tournament tournament, RoundToCreateView roundToCreate, List<Ranking> rankings) {
    PreviousRound previousRoundView = roundToCreate.getPreviousRounds().get(0);
    Round previousRound = roundRepository.findOne(previousRoundView.getRoundId());
    List<Team> teams = rankings.subList(previousRoundView.getTeamsRange().lowerEndpoint() - 1, previousRoundView.getTeamsRange().upperEndpoint())
      .stream()
      .map(Ranking::getTeam)
      .collect(Collectors.toList());
    return convert(roundToCreate, tournament, teams, previousRound);
  }

  private Round convert(RoundToCreateView roundToCreate, Tournament tournament, List<Team> teams, Round previousRound) {
    return RoundBuilder.aRound()
      .withName(roundToCreate.getName())
      .withBranch(TournamentBranch.valueOf(roundToCreate.getTournamentBranch()))
      .withType(RoundType.valueOf(roundToCreate.getType()))
      .withStatus(RoundStatus.CREATED)
      .withFieldRanges(roundToCreate.getFieldRanges())
      .withTournament(tournament)
      .withTeams(teams)
      .withPreviousRound(previousRound)
      .build();
  }
}
