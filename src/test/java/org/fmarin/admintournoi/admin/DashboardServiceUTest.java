package org.fmarin.admintournoi.admin;

import com.google.common.collect.Lists;
import org.fmarin.admintournoi.helper.TimeMachine;
import org.fmarin.admintournoi.payment.PaymentStatus;
import org.fmarin.admintournoi.subscription.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DashboardServiceUTest {

  private DashboardService dashboardService;

  @Mock
  private TournamentRepository mockedTournamentRepository;

  @Before
  public void setUp() {
    dashboardService = new DashboardService(mockedTournamentRepository);
  }

  @Test
  public void getCurrentSubscriptionsStats() {
    // Given
    TimeMachine.useFixedClockAt(LocalDateTime.of(2017, 1, 1, 0, 0));

    Tournament menTournament = TournamentBuilder.aTournament()
      .withId(1L)
      .withGender(Gender.MEN)
      .withTeamLimit(12)
      .withTeams(Lists.newArrayList(
        TeamBuilder.aTeam().withPaymentStatus(PaymentStatus.COMPLETED).build(),
        TeamBuilder.aTeam().withPaymentStatus(PaymentStatus.PENDING).build(),
        TeamBuilder.aTeam().withPaymentStatus(PaymentStatus.COMPLETED).build()))
      .build();
    when(mockedTournamentRepository.findByYearAndGender(2017, Gender.MEN)).thenReturn(menTournament);
    Tournament womenTournament = TournamentBuilder.aTournament()
      .withId(2L)
      .withGender(Gender.WOMEN)
      .withTeamLimit(6)
      .withTeams(Lists.newArrayList(
        TeamBuilder.aTeam().withPaymentStatus(PaymentStatus.COMPLETED).build(),
        TeamBuilder.aTeam().withPaymentStatus(PaymentStatus.COMPLETED).build(),
        TeamBuilder.aTeam().withPaymentStatus(PaymentStatus.PENDING).build(),
        TeamBuilder.aTeam().withPaymentStatus(PaymentStatus.COMPLETED).build()))
      .build();
    when(mockedTournamentRepository.findByYearAndGender(2017, Gender.WOMEN)).thenReturn(womenTournament);


    // When
    Map<String, Object> result = dashboardService.getCurrentSubscriptionsStats();

    // Then
    assertThat(result).containsOnly(
      entry("men_teams_limit", 12),
      entry("men_teams_subscribed", 2),
      entry("women_teams_limit", 6),
      entry("women_teams_subscribed", 3),
      entry("paypal_sales_total", "158,15 €"),
      entry("men_tournament_id", 1L),
      entry("women_tournament_id", 2L)
    );
  }


}