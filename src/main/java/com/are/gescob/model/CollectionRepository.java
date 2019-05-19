package com.are.gescob.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.are.gescob.entity.Collection;

public interface CollectionRepository extends CrudRepository<Collection, Long> {
	
	@Query("FROM Collection ORDER BY name")
	public Iterable<Collection> findAllOrderByName();
}
