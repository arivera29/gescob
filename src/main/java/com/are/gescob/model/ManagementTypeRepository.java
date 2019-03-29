package com.are.gescob.model;

import org.springframework.data.repository.CrudRepository;

import com.are.gescob.entity.ManagementType;

public interface ManagementTypeRepository extends CrudRepository<ManagementType, Long> {
	
	public Iterable<ManagementType> findAllOrderByName();
	
}
