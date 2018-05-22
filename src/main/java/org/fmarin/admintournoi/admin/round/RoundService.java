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
    Round round = convert(roundToCreate, tournamentId);
    roundRepository.save(round);
    poolGenerationService.generatePools(round);
  }

  private Round convert(RoundToCreateView roundToCreate, Long tournamentId) {
    Tournament tournament = tournamentRepository.findOne(tournamentId);
    RoundBuilder roundBuilder = RoundBuilder.aRound()
      .withName(roundToCreate.getName())
      .withBranch(TournamentBranch.valueOf(roundToCreate.getTournamentBranch()))
      .withType(RoundType.valueOf(roundToCreate.getType()))
      .withStatus(RoundStatus.CREATED)
      .withFieldRanges(roundToCreate.getFieldRanges())
      .withTournament(tournament);
    List<Team> teams = tournament.getSubscribedTeams();
    if (!isFirstRound(roundToCreate)) {
      teams = getTeamsForNextRound(roundToCreate);
      roundBuilder.withPreviousRound(roundRepository.findOne(roundToCreate.getPreviousRounds().get(0).getRoundId()));
    }
    roundBuilder.withTeams(teams);
    return roundBuilder.build();
  }

  private boolean isFirstRound(RoundToCreateView roundToCreate) {
    return roundToCreate.getPreviousRounds().isEmpty();
  }

  private List<Team> getTeamsForNextRound(RoundToCreateView view) {
    PreviousRound previousRoundView = view.getPreviousRounds().get(0);
    List<Ranking> rankings = rankingService.getRoundRanking(previousRoundView.getRoundId());
    return rankings.subList(previousRoundView.getTeamsRange().lowerEndpoint() - 1, previousRoundView.getTeamsRange().upperEndpoint())
      .stream()
      .map(Ranking::getTeam)
      .collect(Collectors.toList());
  }
}
