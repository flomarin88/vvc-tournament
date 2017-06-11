package org.fmarin.admintournoi.admin.team;

import com.google.common.collect.Lists;
import org.fmarin.admintournoi.admin.pool.Pool;
import org.fmarin.admintournoi.admin.pool.PoolRepository;
import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.subscription.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    private final PoolRepository poolRepository;

    @Autowired
    public TeamService(PoolRepository poolRepository) {
        this.poolRepository = poolRepository;
    }

    public boolean hasAlreadyPlayedAgainst(Round currentRound, Team team, Team team1, Team team2) {
        List<Round> rounds = getAllPreviousRounds(currentRound);
        List<Pool> pools = poolRepository.findByRoundsAndTeams(rounds, team, team1, team2);
        return !pools.isEmpty();
    }

    List<Round> getAllPreviousRounds(Round round) {
        List<Round> rounds = Lists.newArrayList();
        while (round.getPreviousRound() != null) {
            rounds.add(round.getPreviousRound());
            round = round.getPreviousRound();
        }
        return rounds;
    }
}
