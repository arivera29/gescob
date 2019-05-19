package com.are.gescob.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.are.gescob.entity.Account;



public interface AccountRepository extends CrudRepository<Account, Long> {

	@Query("FROM Account ORDER BY name")
	public Iterable<Account> findAllOrderByName();
	
	@Query("FROM Account WHERE email = :email")
	public Account findByEmail(String email);
	
	
}
