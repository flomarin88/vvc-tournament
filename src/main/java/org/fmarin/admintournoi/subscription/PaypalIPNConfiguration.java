package org.fmarin.admintournoi.subscription;

import com.benfante.paypal.ipnassistant.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaypalIPNConfiguration {

    @Bean
    public IpnConfiguration configuration() {
        IpnConfiguration configuration = new IpnConfiguration();
        configuration.setPaypalIpnUrl("https://ipnpb.sandbox.paypal.com/cgi-bin/webscr");
        configuration.setReceiverEmail("flomarin88+buyer@gmail.com");
        return configuration;
    }

    @Bean
    public IpnMessageBinder messageBinder() {
        return new InMemoryIpnMessageBinder();
    }

    @Bean
    public IpnVerifier verifier() {
        return new DefaultIpnVerifier(configuration());
    }

    @Bean
    public PaymentProcessor paymentProcessor() {
        return new SubscriptionPaymentProcessor();
    }

    @Bean
    public IpnAssistant assistant() {
        return new DefaultIpnAssistant(configuration(), messageBinder(), verifier(), paymentProcessor());
    }
}
