package com.are.gescob.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.are.gescob.entity.Account;
import com.are.gescob.entity.Collector;

public interface CollectorRepository extends CrudRepository<Collector, Long> {

	@Query("FROM Collector ORDER BY name")
	public Iterable<Collector> findAllOrderByName();
	
	@Query("FROM Collector WHERE account = :account ORDER BY name")
	public Iterable<Collector> findAllOrderByName(@Param("account") Account account);
	
	@Query("FROM Collector WHERE account = :account AND state = true ORDER BY name")
	public Iterable<Collector> findActives(@Param("account") Account account);
}
