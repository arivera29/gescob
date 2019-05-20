package com.are.gescob.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.are.gescob.entity.Account;
import com.are.gescob.entity.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {
	@Query("FROM Client ORDER BY name")
	public Iterable<Client> findAllOrderByName();
	
	@Query("FROM Client WHERE account = :account ORDER BY name")
	public Iterable<Client> findAllOrderByName(@Param("account") Account account);
	
	@Query("FROM Client WHERE account = :account AND state = true ORDER BY name")
	public Iterable<Client> findActives(@Param("account") Account account);
}
