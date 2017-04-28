package org.fmarin.admintournoi.payment;

import com.benfante.paypal.ipnassistant.IpnData;
import org.fmarin.admintournoi.helper.TimeMachine;
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
public class IpnMessageInfoBinderUTest {

    private IpnMessageInfoBinder binder;

    @Mock
    private IpnMessageRepository mockedRepository;

    @Before
    public void setUp() throws Exception {
        binder = new IpnMessageInfoBinder(mockedRepository);
    }

    @Test
    public void messageAlreadyExists_should_return_false_when_transaction_does_not_exist() {
        // Given
        String unknownTransactionId = "unknownTransactionId";
        String paymentStatus = "Completed";

        // When
        boolean result = binder.messageAlreadyExists(unknownTransactionId, paymentStatus);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    public void messageAlreadyExists_should_return_true_when_transaction_exists() {
        // Given
        String wellKnownTransactionId = "wellKnownTransactionId";
        String paymentStatus = "Completed";

        when(mockedRepository.findByTransactionIdAndPaymentStatus(wellKnownTransactionId, paymentStatus))
                .thenReturn(new IpnMessage());

        // When
        boolean result = binder.messageAlreadyExists(wellKnownTransactionId, paymentStatus);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    public void add_should_map_ipn_data_to_message_and_store_it() {
        // Given
        Map<String, String> params = new HashMap<>();
        params.put("txn_id", "TEDGSD1235");
        params.put("custom", "12345");
        params.put("payer_email", "flomarin@gmail.com");
        params.put("payment_status", "Completed");
        IpnData ipnData = new IpnData(params);

        LocalDateTime now = LocalDateTime.of(2017, 4, 28, 19, 52, 20);
        TimeMachine.useFixedClockAt(now);

        // When
        binder.add(ipnData);

        // Then
        ArgumentCaptor<IpnMessage> argumentCaptor = ArgumentCaptor.forClass(IpnMessage.class);
        verify(mockedRepository).save(argumentCaptor.capture());
        IpnMessage result = argumentCaptor.getValue();
        assertThat(result.getId()).isNull();
        assertThat(result.getTransactionId()).isEqualTo("TEDGSD1235");
        assertThat(result.getSubscriptionId()).isEqualTo("12345");
        assertThat(result.getPayerEmail()).isEqualTo("flomarin@gmail.com");
        assertThat(result.getPaymentStatus()).isEqualTo("Completed");
        assertThat(result.getReceivedAt()).isEqualTo(now);
    }

}