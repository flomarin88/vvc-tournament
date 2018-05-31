package org.fmarin.admintournoi.admin.round;

import org.fmarin.admintournoi.subscription.Tournament;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoundRepository extends CrudRepository<Round, Long> {

  List<Round> findAllByTournament(Tournament tournament);

  List<Round> findAllByTournamentAndStatus(Tournament tournament, RoundStatus status);
}
