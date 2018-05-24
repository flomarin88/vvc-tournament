package org.fmarin.admintournoi.admin.round;

import com.google.common.collect.Lists;
import org.fmarin.admintournoi.admin.pool.PoolGenerationService;
import org.fmarin.admintournoi.admin.ranking.Ranking;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fmarin.admintournoi.admin.ranking.RankingBuilder.aRanking;
import static org.fmarin.admintournoi.admin.round.RoundBuilder.aRound;
import static org.fmarin.admintournoi.subscription.TeamBuilder.aTeam;
import static org.fmarin.admintournoi.subscription.TournamentBuilder.aTournament;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoundServiceUTest {

  private RoundService service;

  @Mock
  private RoundRepository mockedRoundRepository;
  @Mock
  private TournamentRepository mockedTournamentRepository;
  @Mock
  private PoolGenerationService mockedPoolGenerationService;
    @Mock
  private Round mockedPreviousRound;

  private RoundToCreateView view;
  private Tournament tournament;
  private Round expectedRound;

  private ArgumentCaptor<Round> roundArgumentCaptor = ArgumentCaptor.forClass(Round.class);

  @Before
  public void setUp() {
    service = new RoundService(mockedTournamentRepository, mockedRoundRepository, mockedPoolGenerationService);

    view = new RoundToCreateView();
    view.setName("Tour 1");
    view.setTournamentBranch("PRINCIPALE");
    view.setType("POOL");
    view.setFieldRanges("1-4");

    tournament = aTournament()
      .withId(1L)
      .withTeams(Lists.newArrayList(aTeam().withId(12L).withPaymentStatus("Pending").build()))
      .build();
    when(mockedTournamentRepository.findOne(1L)).thenReturn(tournament);

    expectedRound = aRound()
      .withName("Tour 1")
      .withBranch(TournamentBranch.PRINCIPALE)
      .withType(RoundType.POOL)
      .withStatus(RoundStatus.CREATED)
      .withFieldRanges("1-4")
      .withTournament(tournament)
      .build();
  }

  @Test
  public void create_first_round() {
    // Given
    Team subTeam = aTeam().withId(104L).withPaymentStatus("Completed").build();
    tournament.getTeams().add(subTeam);

    // When
    service.create(1L, view);

    // Then
    expectedRound.setTeams(Lists.newArrayList(subTeam));
    verify(mockedTournamentRepository).findOne(1L);
    verify(mockedRoundRepository).save(roundArgumentCaptor.capture());
    assertThat(roundArgumentCaptor.getValue()).isEqualToComparingFieldByField(expectedRound);
    verify(mockedPoolGenerationService).generatePools(roundArgumentCaptor.capture());
    assertThat(roundArgumentCaptor.getValue()).isEqualToComparingFieldByField(expectedRound);
  }

  @Test
  public void create_next_round() {
    // Given
    view.setFirstPreviousRoundId(10L);
    view.setFirstTeamsFrom(1);
    view.setFirstTeamsTo(2);

    when(mockedRoundRepository.findOne(10L)).thenReturn(mockedPreviousRound);
    Team team1 = aTeam().withId(101L).build();
    Team team2 = aTeam().withId(102L).build();
    Team team3 = aTeam().withId(103L).build();
    List<Ranking> rankings = Lists.newArrayList(
      aRanking().withPosition(1).withTeam(team1).build(),
      aRanking().withPosition(2).withTeam(team2).build(),
      aRanking().withPosition(3).withTeam(team3).build()
    );
    when(mockedPreviousRound.getRankings()).thenReturn(rankings);

    // When
    service.create(1L, view);

    // Then
    expectedRound.setTeams(Lists.newArrayList(team1, team2));
    PreviousRound expectedPreviousRound = PreviousRoundBuilder.aPreviousRound()
      .withPreviousRound(mockedPreviousRound).withTeamsFrom(1).withTeamsTo(2).build();
    expectedRound.setPreviousRounds(Lists.newArrayList(expectedPreviousRound));
    verify(mockedTournamentRepository).findOne(1L);
    verify(mockedRoundRepository).save(roundArgumentCaptor.capture());
    assertThat(roundArgumentCaptor.getValue()).isEqualToComparingFieldByField(expectedRound);
    verify(mockedPoolGenerationService).generatePools(roundArgumentCaptor.capture());
    assertThat(roundArgumentCaptor.getValue()).isEqualToComparingFieldByField(expectedRound);
  }
}