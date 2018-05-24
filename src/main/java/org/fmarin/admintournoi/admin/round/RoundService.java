package org.fmarin.admintournoi.admin.round;

import com.google.common.collect.Lists;
import org.fmarin.admintournoi.admin.pool.PoolGenerationService;
import org.fmarin.admintournoi.admin.ranking.Ranking;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.fmarin.admintournoi.admin.round.PreviousRoundBuilder.*;

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
    RoundBuilder roundBuilder = RoundBuilder.aRound()
      .withName(roundToCreate.getName())
      .withBranch(TournamentBranch.valueOf(roundToCreate.getTournamentBranch()))
      .withType(RoundType.valueOf(roundToCreate.getType()))
      .withStatus(RoundStatus.CREATED)
      .withFieldRanges(roundToCreate.getFieldRanges())
      .withTournament(tournament);
    List<Team> teams = tournament.getSubscribedTeams();
    if (!isFirstRound(roundToCreate)) {
      PreviousRound previousRound = aPreviousRound()
        .withPreviousRound(roundRepository.findOne(roundToCreate.getPreviousRounds().get(0).getRoundId()))
        .withTeamsFrom(roundToCreate.getPreviousRounds().get(0).getTeamsRange().lowerEndpoint() - 1)
        .withTeamsTo(roundToCreate.getPreviousRounds().get(0).getTeamsRange().upperEndpoint())
        .build();
      roundBuilder.withPreviousRounds(Lists.newArrayList(previousRound));
      teams = getTeamsForNextRound(previousRound);
    }
    roundBuilder.withTeams(teams);
    return roundBuilder.build();
  }

  private boolean isFirstRound(RoundToCreateView roundToCreate) {
    return roundToCreate.getPreviousRounds().isEmpty();
  }

  private List<Team> getTeamsForNextRound(PreviousRound previousRound) {
    List<Ranking> rankings = previousRound.getPreviousRound().getRankings();
    return rankings.subList(previousRound.getTeamsFrom(), previousRound.getTeamsTo())
      .stream()
      .map(Ranking::getTeam)
      .collect(Collectors.toList());
  }
}
