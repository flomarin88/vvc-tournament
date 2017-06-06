package org.fmarin.admintournoi.admin.match;

import org.fmarin.admintournoi.admin.pool.Pool;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends CrudRepository<Match, Long> {

    List<Match> findAllByPool(Pool pool) ;
}
