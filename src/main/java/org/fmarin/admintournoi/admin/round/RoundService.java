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
        Round round;
        if (roundToCreate.getPreviousRoundId() == null) {
            round = createFirstRound(tournament, roundToCreate);
            roundRepository.save(round);
            poolGenerationService.generatePools(round);
        } else {
            List<Ranking> rankings = rankingService.getRoundRanking(roundToCreate.getPreviousRoundId());
            round = createNextRound(tournament, roundToCreate, rankings);
            roundRepository.save(round);
            poolGenerationService.generatePools(round, rankings);
        }
    }

    private Round createNextRound(Tournament tournament, RoundToCreateView roundToCreate, List<Ranking> rankings) {
        List<Team> teams;
        Round previousRound = roundRepository.findOne(roundToCreate.getPreviousRoundId());
        teams = rankings.subList(roundToCreate.getTeamsFrom() - 1, roundToCreate.getTeamsTo())
                .stream()
                .map(Ranking::getTeam)
                .collect(Collectors.toList());
        return initRound(tournament, roundToCreate)
                .withTeams(teams)
                .withPreviousRound(previousRound)
                .build();
    }

    private Round createFirstRound(Tournament tournament, RoundToCreateView roundToCreate) {
        List<Team> teams = teamRepository.findAllByTournamentAndPaymentStatusOrderByNameAsc(tournament, "Completed");
        return initRound(tournament, roundToCreate)
                .withTeams(teams)
                .build();
    }

    private RoundBuilder initRound(Tournament tournament, RoundToCreateView roundToCreate) {
        return RoundBuilder.aRound()
                .withName(roundToCreate.getName())
                .withBranch(TournamentBranch.valueOf(roundToCreate.getTournamentBranch()))
                .withType(RoundType.valueOf(roundToCreate.getType()))
                .withStatus(RoundStatus.CREATED)
                .withTournament(tournament)
                .withFieldRanges(roundToCreate.getFieldRanges());
    }
}
