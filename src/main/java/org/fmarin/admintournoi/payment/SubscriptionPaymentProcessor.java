package org.fmarin.admintournoi.payment;

import com.benfante.paypal.ipnassistant.IpnData;
import com.benfante.paypal.ipnassistant.PaymentProcessor;
import org.fmarin.admintournoi.helper.TimeMachine;
import org.fmarin.admintournoi.subscription.Team;
import org.fmarin.admintournoi.subscription.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SubscriptionPaymentProcessor implements PaymentProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionPaymentProcessor.class);

    private final TeamRepository teamRepository;

    @Autowired
    public SubscriptionPaymentProcessor(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public void process(IpnData ipnData) {
        String customData = ipnData.getParameter("custom");
        logger.info("Custom Data : {}", customData);
        Long subscriptionId = Long.valueOf(customData);
        Team team = teamRepository.findOne(subscriptionId);
        team.setPaymentProcessedAt(TimeMachine.now());
        team.setPaymentTransactionId(ipnData.getTransactionId());
        team.setPaymentStatus(ipnData.getPaymentStatus());
        teamRepository.save(team);
        // val code= (100000 + ran.nextInt(900000)).toString()

    }
}
