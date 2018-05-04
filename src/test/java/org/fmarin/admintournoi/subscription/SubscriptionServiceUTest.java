package org.fmarin.admintournoi.subscription;

import org.fmarin.admintournoi.payment.PaymentStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubscriptionServiceUTest {

  private SubscriptionService service;

  @Mock
  private TeamRepository mockedTeamRepository;
  @Mock
  private TournamentRepository mockedTournamentRepository;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void setUp() {
    service = new SubscriptionService(mockedTeamRepository, mockedTournamentRepository);
  }

  @Test
  public void subscribe_should_should_throw_exception_when_tournament_does_not_exist() throws TournamentIsFullException {
    // Given
    Subscription subscriptionWithUnknownTournament = new Subscription();

    expectedException.expect(TournamentIsFullException.class);

    // When
    service.subscribe(subscriptionWithUnknownTournament);

    // Then
    fail("Tournament exists");
  }

  @Test
  public void subscribe_should_throw_exception_when_tournament_limit_is_reached() throws TournamentIsFullException {
    // Given
    Tournament tournament = new Tournament();
    tournament.setTeamLimit(2);
    Team teamCompleted = new Team();
    teamCompleted.setPaymentStatus(PaymentStatus.COMPLETED);
    tournament.setTeams(Arrays.asList(teamCompleted, new Team(), teamCompleted));
    Subscription subscriptionWithLimitReached = new Subscription();
    subscriptionWithLimitReached.setTournamentId(1L);

    when(mockedTournamentRepository.findOne(1L)).thenReturn(tournament);

    expectedException.expect(TournamentIsFullException.class);

    // When
    service.subscribe(subscriptionWithLimitReached);

    // Then
    fail("Tournament is not full");
  }

  @Test
  public void subscribe_should_throw_exception_when_team_with_same_name_as_completed_payment() throws TournamentIsFullException {
    // Given
    Tournament tournament = new Tournament();
    tournament.setTeamLimit(20);
    tournament.setTeams(Arrays.asList(new Team(), new Team()));
    Subscription subscription = new Subscription();
    subscription.setTournamentId(1L);
    subscription.setName("Toto");

    when(mockedTournamentRepository.findOne(1L)).thenReturn(tournament);
    when(mockedTeamRepository.findByNameAndPaymentStatus("Toto", PaymentStatus.COMPLETED)).thenReturn(new Team());

    expectedException.expect(TeamAlreadyExistsException.class);

    // When
    service.subscribe(subscription);

    // Then
    fail("Team does not exist");
  }

  @Test
  public void subscribe_should_return_new_team_id() throws TournamentIsFullException {
    // Given
    Tournament tournament = new Tournament();
    tournament.setTeamLimit(20);
    tournament.setTeams(Arrays.asList(new Team(), new Team()));
    Subscription subscription = new Subscription();
    subscription.setTournamentId(1L);

    when(mockedTournamentRepository.findOne(1L)).thenReturn(tournament);
    Team team = new Team();
    team.setId(100L);
    when(mockedTeamRepository.save(any(Team.class))).thenReturn(team);

    // When
    Team result = service.subscribe(subscription);

    // Then
    assertThat(result.getId()).isEqualTo(100L);
  }

  @Test
  public void create() {
    // Given
    Subscription subscription = new Subscription();
    subscription.setName("Equipe");
    subscription.setLevel(1);
    subscription.setCaptainName("Captain");
    subscription.setCaptainEmail("Captain email");
    subscription.setCaptainPhone("0123456789");
    subscription.setCaptainClub("VVC");
    subscription.setPlayer2Name("Player 2");
    subscription.setPlayer2Email("Player 2 Email");
    subscription.setPlayer2Club("VVC 2");
    subscription.setPlayer3Name("Player 3");
    subscription.setPlayer3Email("Player 3 Email");
    subscription.setPlayer3Club("VVC 3");
    Tournament tournament = new Tournament();

    // When
    Team result = service.create(subscription, tournament);

    // Then
    assertThat(result.getId()).isNull();
    assertThat(result.getTournament()).isEqualTo(tournament);
    assertThat(result.getName()).isEqualTo("Equipe");
    assertThat(result.getLevel()).isEqualTo(Level.NATIONAL);
    assertThat(result.getCaptainName()).isEqualTo("Captain");
    assertThat(result.getCaptainEmail()).isEqualTo("Captain email");
    assertThat(result.getCaptainPhone()).isEqualTo("0123456789");
    assertThat(result.getCaptainClub()).isEqualTo("VVC");
    assertThat(result.getPlayer2Name()).isEqualTo("Player 2");
    assertThat(result.getPlayer2Email()).isEqualTo("Player 2 Email");
    assertThat(result.getPlayer2Club()).isEqualTo("VVC 2");
    assertThat(result.getPlayer3Name()).isEqualTo("Player 3");
    assertThat(result.getPlayer3Email()).isEqualTo("Player 3 Email");
    assertThat(result.getPlayer3Club()).isEqualTo("VVC 3");
    assertThat(result.getPaymentStatus()).isEqualTo(PaymentStatus.PENDING);
  }
}