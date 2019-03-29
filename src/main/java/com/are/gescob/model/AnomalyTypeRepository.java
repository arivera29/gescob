package com.are.gescob.model;

import org.springframework.data.repository.CrudRepository;

import com.are.gescob.entity.AnomalyType;

public interface AnomalyTypeRepository extends CrudRepository<AnomalyType, Long> {
	
	public Iterable<AnomalyType> findAllOrderByName();
}
