package org.fmarin.admintournoi.admin.match;

import com.google.common.collect.Lists;
import org.fmarin.admintournoi.admin.pool.Pool;
import org.fmarin.admintournoi.admin.round.Round;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.fmarin.admintournoi.admin.match.MatchBuilder.aMatch;

@Service
public class MatchGenerationService {

    public void generate(Round round) {
        round.getPools().parallelStream().forEach(this::generatePoolMatches);
    }

    void generatePoolMatches(Pool pool) {
        List<Match> matches = Lists.newArrayList(
                aMatch().withTeam1(pool.getTeam1()).withTeam2(pool.getTeam3()).withPool(pool).build(),
                aMatch().withTeam1(pool.getTeam2()).withTeam2(pool.getTeam3()).withPool(pool).build(),
                aMatch().withTeam1(pool.getTeam1()).withTeam2(pool.getTeam2()).withPool(pool).build()
        );
        pool.setMatches(matches);
    }

}
