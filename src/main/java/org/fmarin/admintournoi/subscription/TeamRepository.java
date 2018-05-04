package org.fmarin.admintournoi.subscription;

import org.fmarin.admintournoi.payment.PaymentStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {

    Team findByNameAndPaymentStatus(String name, PaymentStatus paymentStatus);

    List<Team> findAllByTournamentAndPaymentStatusOrderByNameAsc(Tournament tournament, PaymentStatus paymentStatus);
}
