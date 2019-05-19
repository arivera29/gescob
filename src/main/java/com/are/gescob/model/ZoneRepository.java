package com.are.gescob.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.are.gescob.entity.Zone;

public interface ZoneRepository extends CrudRepository<Zone, Long> {
	
	@Query("FROM Zone ORDER BY name")
	public Iterable<Zone> findAllOrderByName();
}
