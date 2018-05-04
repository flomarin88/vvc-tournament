package org.fmarin.admintournoi.payment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpnMessageRepository extends CrudRepository<IpnMessage, Long> {

    IpnMessage findByTransactionIdAndPaymentStatus(String transactionId, PaymentStatus paymentStatus);
}
