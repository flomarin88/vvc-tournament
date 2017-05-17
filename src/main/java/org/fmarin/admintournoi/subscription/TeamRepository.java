package org.fmarin.admintournoi.subscription;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {

    Team findByNameAndPaymentStatus(String name, String paymentStatus);

    List<Team> findAllByTournamentAndPaymentStatusOrderByNameAsc(Tournament tournament, String paymentStatus);
}
