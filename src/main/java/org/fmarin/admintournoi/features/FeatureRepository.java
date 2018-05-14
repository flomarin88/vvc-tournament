package org.fmarin.admintournoi.features;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureRepository extends CrudRepository<Feature, Long> {
  Feature findByName(String name);
}
