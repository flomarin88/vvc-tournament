package org.fmarin.admintournoi.admin.ranking;

import com.google.common.collect.Maps;
import org.fmarin.admintournoi.admin.match.Match;
import org.fmarin.admintournoi.admin.match.MatchRepository;
import org.fmarin.admintournoi.admin.match.MatchStatus;
import org.fmarin.admintournoi.admin.pool.Pool;
import org.fmarin.admintournoi.admin.pool.PoolRepository;
import org.fmarin.admintournoi.subscription.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class RankingService {

    private final MatchRepository matchRepository;
    private final PoolRepository poolRepository;

    @Autowired
    public RankingService(MatchRepository matchRepository, PoolRepository poolRepository) {
        this.matchRepository = matchRepository;
        this.poolRepository = poolRepository;
    }

    public List<Ranking> getPoolRanking(Long poolId) {
        Pool pool = poolRepository.findOne(poolId);
        List<Match> matches = matchRepository.findAllByPool(pool);
        Map<Team, Ranking> rankings = Maps.newHashMap();
        for (Match match : matches) {
            updateRanking(get(rankings, match.getTeam1()), match, match.getTeam1());
            updateRanking(get(rankings, match.getTeam2()), match, match.getTeam2());
        }
        return rankings.values().stream()
                .sorted(poolRankingOrdering())
                .collect(Collectors.toList());
    }

    private void updateRanking(Ranking ranking, Match match, Team team) {
        ranking.setPointsFor(ranking.getPointsFor() + match.getPointsFor(team));
        ranking.setPointsAgainst(ranking.getPointsAgainst() + match.getPointsAgainst(team));
        MatchStatus status = match.getStatus(team);
        ranking.setVictories(ranking.getVictories() + (MatchStatus.VICTORY.equals(status) ? 1 : 0));
        ranking.setDefeats(ranking.getDefeats() + (MatchStatus.DEFEAT.equals(status) ? 1 : 0));
        ranking.setDifference(ranking.getPointsFor() - ranking.getPointsAgainst());
    }

    private Ranking get(Map<Team, Ranking> rankings, Team team) {
        return rankings.computeIfAbsent(team, Ranking::new);
    }

    private Comparator<Ranking> poolRankingOrdering() {
        return Comparator.comparing(Ranking::getVictories)
                .thenComparing(Ranking::getDifference)
                .thenComparing(Ranking::getPointsFor)
                .reversed();
    }
}
