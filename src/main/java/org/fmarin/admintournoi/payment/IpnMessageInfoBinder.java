package org.fmarin.admintournoi.payment;

import com.benfante.paypal.ipnassistant.IpnData;
import com.benfante.paypal.ipnassistant.IpnMessageBinder;
import org.fmarin.admintournoi.helper.TimeMachine;
import org.springframework.beans.factory.annotation.Autowired;

public class IpnMessageInfoBinder implements IpnMessageBinder {

    private final IpnMessageRepository repository;

    @Autowired
    public IpnMessageInfoBinder(IpnMessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean messageAlreadyExists(String transactionId, String paymentStatus) {
        IpnMessage message = repository.findByTransactionIdAndPaymentStatus(transactionId, paymentStatus);
        return message != null;
    }

    @Override
    public void add(IpnData ipnData) {
        IpnMessage message = new IpnMessage();
        message.setTransactionId(ipnData.getTransactionId());
        message.setSubscriptionId(ipnData.getParameter("custom"));
        message.setPayerEmail(ipnData.getParameter("payer_email"));
        message.setPaymentStatus(ipnData.getPaymentStatus());
        message.setTimestamp(TimeMachine.now());
        repository.save(message);
    }
}
