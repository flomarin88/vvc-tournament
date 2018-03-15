package org.fmarin.admintournoi.subscription;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends CrudRepository<Tournament, Long> {
    Tournament findByYearAndGender(int year, Gender gender);
}
