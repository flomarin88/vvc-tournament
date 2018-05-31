package org.fmarin.admintournoi.rounds;

import com.google.common.collect.Lists;
import org.fmarin.admintournoi.admin.round.RoundRepository;
import org.fmarin.admintournoi.admin.round.RoundStatus;
import org.fmarin.admintournoi.subscription.Gender;
import org.fmarin.admintournoi.subscription.Tournament;
import org.fmarin.admintournoi.subscription.TournamentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.fmarin.admintournoi.subscription.TournamentBuilder.aTournament;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoundPublicControllerUTest {

  private RoundPublicController controller;

  @Mock
  private TournamentRepository mockedTournamentRepository;
  @Mock
  private RoundRepository mockedRoundRepository;

  @Before
  public void setUp() {
    controller = new RoundPublicController(mockedRoundRepository, null, mockedTournamentRepository);
  }

  @Test
  public void index() {
    // Given
    Tournament tournament = aTournament().withId(1L).withGender(Gender.MEN).withName("3x3").build();
    when(mockedTournamentRepository.findOne(1L)).thenReturn(tournament);

    when(mockedRoundRepository.findAllByTournamentAndStatus(tournament, RoundStatus.STARTED)).thenReturn(Lists.newArrayList());

    // When
    ModelAndView result = controller.index(1L);

    // Then
    List<RoundPublicView> expectedRoundsView = Lists.newArrayList(
      new RoundPublicView("principale", "primary", "Principal - Aucun tour", true),
      new RoundPublicView("super_consolante", "info", "Super Consolante - Aucun tour", true),
      new RoundPublicView("consolante", "default", "Consolante - Aucun tour", true)
    );
    assertThat(result.getViewName()).isEqualTo("/public/rounds");
    assertThat(result.getModel()).contains(
      entry("tournamentId", 1L),
      entry("tournamentName", "3x3 Masculin"),
      entry("rounds", expectedRoundsView)
    );
  }
}