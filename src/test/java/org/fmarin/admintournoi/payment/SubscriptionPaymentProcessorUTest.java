package org.fmarin.admintournoi.payment;

import com.benfante.paypal.ipnassistant.IpnData;
import org.fmarin.admintournoi.helper.TimeMachine;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.TeamRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubscriptionPaymentProcessorUTest {

    private SubscriptionPaymentProcessor processor;

    @Mock
    private TeamRepository mockedTeamRepository;

    @Before
    public void setUp() throws Exception {
        processor = new SubscriptionPaymentProcessor(mockedTeamRepository);
    }

    @Test
    public void process_should_update_team_payment_informations() {
        // Given
        Map<String, String> params = new HashMap<>();
        params.put("txn_id", "TEDGSD1235");
        params.put("custom", "12345");
        params.put("payer_email", "flomarin@gmail.com");
        params.put("payment_status", "Completed");
        IpnData ipnData = new IpnData(params);

        LocalDateTime now = LocalDateTime.of(2017, 4, 28, 19, 52, 20);
        TimeMachine.useFixedClockAt(now);
        when(mockedTeamRepository.findOne(12345L)).thenReturn(new Team());

        // When
        processor.process(ipnData);
        
        // Then
        ArgumentCaptor<Team> argumentCaptor = ArgumentCaptor.forClass(Team.class);
        verify(mockedTeamRepository).save(argumentCaptor.capture());
        Team updatedTeam = argumentCaptor.getValue();
        assertThat(updatedTeam.getPaymentTransactionId()).isEqualTo("TEDGSD1235");
        assertThat(updatedTeam.getPaymentStatus()).isEqualTo("Completed");
        assertThat(updatedTeam.getPaymentProcessedAt()).isEqualTo(now);
    }
}