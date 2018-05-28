package org.fmarin.admintournoi.admin.round;

import org.fmarin.admintournoi.admin.pool.PoolGenerationService;
import org.fmarin.admintournoi.admin.ranking.Ranking;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.fmarin.admintournoi.admin.round.PreviousRoundBuilder.aPreviousRound;

@Service
public class RoundService {

  private final TournamentRepository tournamentRepository;
  private final RoundRepository roundRepository;
  private final PoolGenerationService poolGenerationService;

  @Autowired
  public RoundService(TournamentRepository tournamentRepository,
                      RoundRepository roundRepository, PoolGenerationService poolGenerationService) {
    this.tournamentRepository = tournamentRepository;
    this.roundRepository = roundRepository;
    this.poolGenerationService = poolGenerationService;
  }

  public void create(Long tournamentId, RoundToCreateView roundToCreate) {
    Round round = convert(roundToCreate, tournamentId);
    roundRepository.save(round);
    poolGenerationService.generatePools(round);
  }

  private Round convert(RoundToCreateView roundToCreate, Long tournamentId) {
    Tournament tournament = tournamentRepository.findOne(tournamentId);
    RoundBuilder roundBuilder = build(roundToCreate, tournament);
    if (isFirstRound(roundToCreate)) {
      List<Team> teams = tournament.getSubscribedTeams();
      roundBuilder.withTeams(teams);
    }
    else {
      List<PreviousRound> previousRounds = getPreviousRounds(roundToCreate);
      roundBuilder.withPreviousRounds(previousRounds);
      roundBuilder.withTeams(getTeams(previousRounds));
    }
    return roundBuilder.build();
  }

  private RoundBuilder build(RoundToCreateView roundToCreate, Tournament tournament) {
    return RoundBuilder.aRound()
        .withName(roundToCreate.getName())
        .withBranch(TournamentBranch.valueOf(roundToCreate.getTournamentBranch()))
        .withType(RoundType.valueOf(roundToCreate.getType()))
        .withStatus(RoundStatus.CREATED)
        .withFieldRanges(roundToCreate.getFieldRanges())
        .withTournament(tournament);
  }

  private List<PreviousRound> getPreviousRounds(RoundToCreateView roundToCreate) {
    return roundToCreate.getPreviousRounds().parallelStream()
          .map(previousRoundView ->
            aPreviousRound()
              .withPreviousRound(roundRepository.findOne(previousRoundView.getRoundId()))
              .withTeamsFrom(previousRoundView.getTeamsRange().lowerEndpoint())
              .withTeamsTo(previousRoundView.getTeamsRange().upperEndpoint())
              .build())
          .collect(Collectors.toList());
  }

  private boolean isFirstRound(RoundToCreateView roundToCreate) {
    return roundToCreate.getPreviousRounds().isEmpty();
  }

  private List<Team> getTeams(List<PreviousRound> previousRounds) {
    return previousRounds.parallelStream()
      .map(this::getTeams)
      .flatMap(List::stream)
      .collect(Collectors.toList());
  }

  private List<Team> getTeams(PreviousRound previousRound) {
    List<Ranking> rankings = previousRound.getPreviousRound().getRankings();
    return rankings.subList(previousRound.getTeamsFrom() - 1, previousRound.getTeamsTo())
      .stream()
      .map(Ranking::getTeam)
      .collect(Collectors.toList());
  }
}
