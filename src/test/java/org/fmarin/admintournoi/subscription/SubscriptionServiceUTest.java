package org.fmarin.admintournoi.subscription;

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
    public void setUp() throws Exception {
        service = new SubscriptionService(mockedTeamRepository, mockedTournamentRepository);
    }

    @Test
    public void subscribe_should_return_null_when_tournament_does_not_exist() throws TournamentIsFullException {
        // Given
        Subscription subscriptionWithUnknownTournament = new Subscription();

        expectedException.expect(TournamentIsFullException.class);

        // When
        service.subscribe(subscriptionWithUnknownTournament);

        // Then
        fail("Tournament exists");
    }

    @Test
    public void subscribe_should_return_null_when_tournament_limit_is_reached() throws TournamentIsFullException {
        // Given
        Tournament tournament = new Tournament();
        tournament.setTeamLimit(2);
        Team teamCompleted = new Team();
        teamCompleted.setPaymentStatus("Completed");
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
        subscription.setPlayer2Name("Player 2");
        subscription.setPlayer2Email("Player 2 Email");
        subscription.setPlayer3Name("Player 3");
        subscription.setPlayer3Email("Player 3 Email");
        Tournament tournament = new Tournament();

        // When
        Team result = service.create(subscription, tournament);

        // Then
        assertThat(result.getId()).isNull();
        assertThat(result.getTournament()).isEqualTo(tournament);
        assertThat(result.getName()).isEqualTo("Equipe");
        assertThat(result.getLevel()).isEqualTo(1);
        assertThat(result.getCaptainName()).isEqualTo("Captain");
        assertThat(result.getCaptainEmail()).isEqualTo("Captain email");
        assertThat(result.getCaptainPhone()).isEqualTo("0123456789");
        assertThat(result.getPlayer2Name()).isEqualTo("Player 2");
        assertThat(result.getPlayer2Email()).isEqualTo("Player 2 Email");
        assertThat(result.getPlayer3Name()).isEqualTo("Player 3");
        assertThat(result.getPlayer3Email()).isEqualTo("Player 3 Email");
    }
}