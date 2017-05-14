package org.fmarin.admintournoi.admin.checkin;

import org.fmarin.admintournoi.subscription.Level;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.TeamRepository;
import org.fmarin.admintournoi.subscription.Tournament;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CheckInControllerUTest {

    private CheckInController controller;

    @Mock
    private TeamRepository mockedTeamRepository;

    @Before
    public void setUp() throws Exception {
        controller = new CheckInController(mockedTeamRepository);
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
        team.setPaymentVerficationCode(741451);
        
        // When
        TeamToCheckInView result = controller.convert(team);
        
        // Then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Popol");
        assertThat(result.getTournamentLabel()).isEqualTo("3x3 Féminin");
        assertThat(result.getLevelLabel()).isEqualTo("Régional");
        assertThat(result.getCaptainName()).isEqualTo("Mister Paul");
        assertThat(result.getCaptainPhone()).isEqualTo("0123456789");
        assertThat(result.getCaptainEmail()).isEqualTo("popol@gmail.com");
        assertThat(result.getPaymentVerficationCode()).isEqualTo(741451);
        assertThat(result.isPresent()).isFalse();
    }
    
    @Test
    public void index() {
        // Given
        List<Team> teams = Arrays.asList();
        
        // When
        ModelAndView result = controller.index();
        
        // Then
        assertThat(result.getViewName()).isEqualTo("teams");
        List<TeamToCheckInView> teamsResult = (List<TeamToCheckInView>) result.getModel().get("teams");
        assertThat(teamsResult).hasSize(2);
    }
}