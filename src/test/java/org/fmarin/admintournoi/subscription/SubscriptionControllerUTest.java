package org.fmarin.admintournoi.subscription;

import org.assertj.core.groups.Tuple;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubscriptionControllerUTest {

  private SubscriptionController controller;

  @Mock
  private TournamentRepository mockedTournamentRepository;

  @Before
  public void setUp() {
    controller = new SubscriptionController(null, null, mockedTournamentRepository, null, null);
  }

  @Test
  public void index_should_return_list_of_subscribed_teams_by_tournament() {
    // Given
    Tournament menTournament = TournamentBuilder.aTournament()
      .withName("3x3")
      .withGender(Gender.MEN)
      .withTeams(Lists.newArrayList(
        TeamBuilder.aTeam().withName("Team 1").withCaptainName("Player 11").withPlayer2Name("Player 12").withPlayer3Name("Player 13").withPaymentStatus("Completed").build(),
        TeamBuilder.aTeam().withName("Team 2").withCaptainName("Player 21").withPlayer2Name("Player 22").withPlayer3Name("Player 23").withPaymentStatus("Completed").build(),
        TeamBuilder.aTeam().withName("Team 3").withCaptainName("Player 31").withPlayer2Name("Player 32").withPlayer3Name("Player 33").withPaymentStatus("In progress").build()
        )
      )
      .build();

    Tournament womenTournament = TournamentBuilder.aTournament()
      .withName("3x3")
      .withGender(Gender.WOMEN)
      .build();

    when(mockedTournamentRepository.findByYearAndGender(2018, Gender.MEN)).thenReturn(menTournament);
    when(mockedTournamentRepository.findByYearAndGender(2018, Gender.WOMEN)).thenReturn(womenTournament);

    // When
    ModelAndView result = controller.index();

    // Then
    List<TeamsByTournamentView> tournaments = (List<TeamsByTournamentView>) result.getModel().get("tournaments");
    assertThat(tournaments)
      .hasSize(2)
      .extracting("name")
      .containsExactly("3x3 Féminin", "3x3 Masculin");
    assertThat(tournaments.get(1).getTeams()).hasSize(2)
      .extracting("index", "name", "players")
      .containsExactly(
        Tuple.tuple(1, "Team 1", Lists.newArrayList("Player 11", "Player 12", "Player 13")),
        Tuple.tuple(2, "Team 2", Lists.newArrayList("Player 21", "Player 22", "Player 23"))
      );
  }
}