package com.are.gescob.model;

import org.springframework.data.repository.CrudRepository;

import com.are.gescob.entity.Zone;

public interface ZoneRepository extends CrudRepository<Zone, Long> {
	
	public Iterable<Zone> findAllOrderByName();
}
