package org.fmarin.admintournoi.admin.checkin;

import org.fmarin.admintournoi.fixtures.FixtureTeam;
import org.fmarin.admintournoi.subscription.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckInControllerUTest {

    private CheckInController controller;

    @Mock
    private TeamRepository mockedTeamRepository;
    @Mock
    private TournamentRepository mockedTournamentRepository;

    @Before
    public void setUp() {
        controller = new CheckInController(mockedTeamRepository, mockedTournamentRepository);
    }

    @Test
    public void convert() {
        // Given
        Tournament tournament = new Tournament();
        tournament.setName("3x3 Féminin");

        Team team = new Team();
        team.setId(1L);
        team.setName("Popol");
        team.setTournament(tournament);
        team.setLevel(Level.REGIONAL);
        team.setCaptainName("Mister Paul");
        team.setCaptainEmail("popol@gmail.com");
        team.setCaptainPhone("0123456789");
        team.setPaymentVerificationCode(741451);
        team.setPresent(true);

        // When
        TeamToCheckInView result = controller.convert(team);

        // Then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Popol");
        assertThat(result.getLevelLabel()).isEqualTo("Régional");
        assertThat(result.getCaptainName()).isEqualTo("Mister Paul");
        assertThat(result.getCaptainPhone()).isEqualTo("0123456789");
        assertThat(result.getCaptainEmail()).isEqualTo("popol@gmail.com");
        assertThat(result.getPaymentVerificationCode()).isEqualTo(741451);
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    public void index() {
        // Given
        Tournament tournament = TournamentBuilder.aTournament().withName("Tournoi").build();
        when(mockedTournamentRepository.findOne(1L)).thenReturn(tournament);

        List<Team> teams = Arrays.asList(
                FixtureTeam.withDefaultValues().build(),
                FixtureTeam.withDefaultValues().build());
        when(mockedTeamRepository.findAllByTournamentAndPaymentStatusOrderByNameAsc(tournament, "Completed"))
                .thenReturn(teams);

        // When
        ModelAndView result = controller.index(1L);

        // Then
        assertThat(result.getViewName()).isEqualTo("admin/checkin");
        assertThat(result.getModel()).containsKeys("teams", "tournament", "absenceCount");
    }
}