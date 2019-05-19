package com.are.gescob.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.are.gescob.entity.AnomalyType;

public interface AnomalyTypeRepository extends CrudRepository<AnomalyType, Long> {
	@Query("FROM AnomalyType ORDER BY name")
	public Iterable<AnomalyType> findAllOrderByName();
}
