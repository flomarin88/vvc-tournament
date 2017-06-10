package org.fmarin.admintournoi.admin.pool;

import org.fmarin.admintournoi.admin.round.Round;
import org.fmarin.admintournoi.subscription.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoolRepository extends CrudRepository<Pool, Long> {

    List<Pool> findAllByRoundOrderByPosition(Round round);
    @Query("SELECT pool FROM Pool pool WHERE pool.round = ?1 AND ( pool.team1 = ?2 OR pool.team2 = ?2 OR pool.team3 = ?2)")
    Pool findByRoundAndTeam(Round round, Team team);
}
