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
    @Query("SELECT pool FROM Pool pool WHERE pool.round IN ?1 AND ( pool.team1 = ?2 OR pool.team2 = ?2 OR pool.team3 = ?2)" +
            "AND (pool.team1 = ?3 OR pool.team2 = ?3 OR pool.team3 = ?3 OR pool.team1 = ?4 OR pool.team2 = ?4 OR pool.team3 = ?4)")
    List<Pool> findByRoundsAndTeams(List<Round> rounds, Team team1, Team team2, Team team3);
}
