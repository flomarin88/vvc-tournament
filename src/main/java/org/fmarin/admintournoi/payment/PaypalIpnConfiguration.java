package org.fmarin.admintournoi.payment;

import com.benfante.paypal.ipnassistant.*;
import org.fmarin.admintournoi.mailing.MailChimpService;
import org.fmarin.admintournoi.subscription.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaypalIpnConfiguration {

    @Autowired
    private IpnMessageRepository ipnMessageRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private MailChimpService mailChimpService;

    @Bean
    public IpnConfiguration configuration() {
        IpnConfiguration configuration = new IpnConfiguration();
        configuration.setPaypalIpnUrl("https://ipnpb.sandbox.paypal.com/cgi-bin/webscr");
        configuration.setReceiverEmail("flomarin88+buyer@gmail.com");
        return configuration;
    }

    @Bean
    public IpnMessageBinder messageBinder() {
        return new IpnMessageInfoBinder(ipnMessageRepository);
    }

    @Bean
    public IpnVerifier verifier() {
        return new DefaultIpnVerifier(configuration());
    }

    @Bean
    public PaymentProcessor paymentProcessor() {
        return new SubscriptionPaymentProcessor(teamRepository, mailChimpService);
    }

    @Bean
    public IpnAssistant assistant() {
        return new DefaultIpnAssistant(configuration(), messageBinder(), verifier(), paymentProcessor());
    }
}
