package com.are.gescob.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.are.gescob.entity.Account;
import com.are.gescob.entity.Zone;

public interface ZoneRepository extends CrudRepository<Zone, Long> {
	
	@Query("FROM Zone WHERE account = :account ORDER BY name")
	public Iterable<Zone> findAllOrderByName(@Param("account") Account account);
	
	@Query("FROM Zone WHERE account = :account AND state = true ORDER BY name")
	public Iterable<Zone> findActives(@Param("account")Account account);
}
