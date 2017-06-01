package org.fmarin.admintournoi.admin.pool;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoolRepository extends CrudRepository<Pool, Long> {

    List<Pool> findAllByRoundOrderByPosition(Round round);
}
