package com.are.gescob.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.are.gescob.entity.ResultType;



public interface ResultTypeRepository extends CrudRepository<ResultType, Long> {
	
	@Query("FROM ResultType ORDER BY name")
	public Iterable<ResultType> findAllOrderByName();
}
