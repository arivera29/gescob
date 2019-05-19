package com.are.gescob.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.are.gescob.entity.ManagementType;

public interface ManagementTypeRepository extends CrudRepository<ManagementType, Long> {
	
	@Query("FROM ManagementType ORDER BY name")
	public Iterable<ManagementType> findAllOrderByName();
	
}
