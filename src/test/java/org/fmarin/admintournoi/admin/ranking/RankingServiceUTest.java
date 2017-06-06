package org.fmarin.admintournoi.admin.ranking;

import com.google.common.collect.Lists;
import org.fmarin.admintournoi.admin.match.Match;
import org.fmarin.admintournoi.admin.match.MatchRepository;
import org.fmarin.admintournoi.admin.pool.Pool;
import org.fmarin.admintournoi.admin.pool.PoolRepository;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.TeamBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fmarin.admintournoi.admin.match.MatchBuilder.aMatch;
import static org.fmarin.admintournoi.admin.pool.PoolBuilder.aPool;
import static org.fmarin.admintournoi.admin.ranking.RankingBuilder.*;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class RankingServiceUTest {

    @InjectMocks
    private RankingService service;

    @Mock
    private MatchRepository mockedMatchRepository;
    @Mock
    private PoolRepository mockedPoolRepository;

    @Test
    public void getPoolRanking() {
        // Given
        Pool pool = aPool().build();
        when(mockedPoolRepository.findOne(1L)).thenReturn(pool);

        Team teamA = TeamBuilder.aTeam().withId(1L).build();
        Team teamB = TeamBuilder.aTeam().withId(2L).build();
        Team teamC = TeamBuilder.aTeam().withId(3L).build();

        Match match1 = aMatch()
                .withTeam1(teamA).withScoreTeam1(21)
                .withTeam2(teamB).withScoreTeam2(15)
                .build();
        Match match2 = aMatch()
                .withTeam1(teamA).withScoreTeam1(12)
                .withTeam2(teamC).withScoreTeam2(21)
                .build();
        Match match3 = aMatch()
                .withTeam1(teamC).withScoreTeam1(21)
                .withTeam2(teamB).withScoreTeam2(19)
                .build();

        when(mockedMatchRepository.findAllByPool(pool)).thenReturn(Lists.newArrayList(match1, match2, match3));

        // When
        List<Ranking> result = service.getPoolRanking(1L);

        // Then
        Ranking ranking1 = aRanking()
                .withTeam(teamC)
                .withVictories(2)
                .withDefeats(0)
                .withPointsFor(42)
                .withPointsAgainst(31)
                .withDifference(11)
                .build();
        Ranking ranking2 = aRanking()
                .withTeam(teamA)
                .withVictories(1)
                .withDefeats(1)
                .withPointsFor(33)
                .withPointsAgainst(36)
                .withDifference(-3)
                .build();
        Ranking ranking3 = aRanking()
                .withTeam(teamB)
                .withVictories(0)
                .withDefeats(2)
                .withPointsFor(34)
                .withPointsAgainst(42)
                .withDifference(-8)
                .build();
        assertThat(result).hasSize(3)
                .containsSequence(ranking1, ranking2, ranking3);
    }
}