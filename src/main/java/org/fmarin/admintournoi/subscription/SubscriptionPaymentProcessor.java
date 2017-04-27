package org.fmarin.admintournoi.subscription;

import com.benfante.paypal.ipnassistant.DefaultIpnAssistant;
import com.benfante.paypal.ipnassistant.IpnData;
import com.benfante.paypal.ipnassistant.PaymentProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscriptionPaymentProcessor implements PaymentProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DefaultIpnAssistant.class);

    @Override
    public void process(IpnData ipnData) {
        Long subscriptionId = Long.valueOf(ipnData.getParameter("custom"));
        logger.info("SubscriptionId {}", subscriptionId.toString());
    }
}
