package org.fmarin.admintournoi.admin.round;

import org.fmarin.admintournoi.admin.pool.PoolGenerationService;
import org.fmarin.admintournoi.admin.ranking.Ranking;
import org.fmarin.admintournoi.admin.ranking.RankingService;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.TeamRepository;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoundService {

    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final RoundRepository roundRepository;
    private final PoolGenerationService poolGenerationService;
    private final RankingService rankingService;

    @Autowired
    public RoundService(TournamentRepository tournamentRepository, TeamRepository teamRepository,
                        RoundRepository roundRepository, PoolGenerationService poolGenerationService,
                        RankingService rankingService) {
        this.tournamentRepository = tournamentRepository;
        this.teamRepository = teamRepository;
        this.roundRepository = roundRepository;
        this.poolGenerationService = poolGenerationService;
        this.rankingService = rankingService;
    }

    public void create(Long tournamentId, RoundToCreateView roundToCreate) {
        Tournament tournament = tournamentRepository.findOne(tournamentId);
        Round previousRound = null;
        List<Team> teams;
        if (roundToCreate.getPreviousRoundId() == null) {
            teams = teamRepository.findAllByTournamentAndPaymentStatusOrderByNameAsc(tournament, "Completed");
        } else {
            previousRound = roundRepository.findOne(roundToCreate.getPreviousRoundId());
            List<Ranking> roundRanking = rankingService.getRoundRanking(roundToCreate.getPreviousRoundId());
            teams = roundRanking.subList(roundToCreate.getTeamsFrom() - 1, roundToCreate.getTeamsTo())
                    .stream()
                    .map(Ranking::getTeam)
                    .collect(Collectors.toList());
        }
        Round round = RoundBuilder.aRound()
                .withName(roundToCreate.getName())
                .withBranch(TournamentBranch.valueOf(roundToCreate.getTournamentBranch()))
                .withType(RoundType.valueOf(roundToCreate.getType()))
                .withStatus(RoundStatus.CREATED)
                .withPreviousRound(previousRound)
                .withTeams(teams)
                .withTournament(tournament)
                .withFieldRanges(roundToCreate.getFieldRanges())
                .build();
        roundRepository.save(round);
        poolGenerationService.generatePools(round);
    }
}
