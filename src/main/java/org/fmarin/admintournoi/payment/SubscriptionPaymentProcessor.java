package org.fmarin.admintournoi.payment;

import com.benfante.paypal.ipnassistant.IpnData;
import com.benfante.paypal.ipnassistant.PaymentProcessor;
import org.fmarin.admintournoi.helper.TimeMachine;
import org.fmarin.admintournoi.mailing.MailChimpService;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public class SubscriptionPaymentProcessor implements PaymentProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionPaymentProcessor.class);

    private final TeamRepository teamRepository;
    private final MailChimpService mailService;

    @Autowired
    public SubscriptionPaymentProcessor(TeamRepository teamRepository, MailChimpService mailService) {
        this.teamRepository = teamRepository;
        this.mailService = mailService;
    }

    @Override
    public void process(IpnData ipnData) {
        Team team = validateSubscription(ipnData);
        if (team != null) {
            subscibeTeamToMailChimp(team);
        }
    }

    private Team validateSubscription(IpnData ipnData) {
        String customData = ipnData.getParameter("custom");
        logger.info("Subscription Id : {}", customData);
        Long subscriptionId = Long.valueOf(customData);
        Team team = teamRepository.findOne(subscriptionId);
        if (team != null) {
            team.setPaymentProcessedAt(TimeMachine.now());
            team.setPaymentTransactionId(ipnData.getTransactionId());
            team.setPaymentStatus(ipnData.getPaymentStatus());
            Random rnd = new Random();
            int code = 100000 + rnd.nextInt(900000);
            team.setPaymentVerficationCode(code);
            logger.info("{} updated and subscription validated", team.toString());
            return teamRepository.save(team);
        }
        logger.warn("Team with id {} not found", customData);
        return null;
    }

    private void subscibeTeamToMailChimp(Team team) {
        mailService.subscribe(team.getCaptainEmail(), team.getName(), team.getPaymentVerficationCode());
    }
}
