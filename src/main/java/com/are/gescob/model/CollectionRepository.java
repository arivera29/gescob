package com.are.gescob.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.are.gescob.entity.Account;
import com.are.gescob.entity.Collection;

public interface CollectionRepository extends CrudRepository<Collection, Long> {
	
	@Query("FROM Collection WHERE account = :account ORDER BY name")
	public Iterable<Collection> findAllOrderByName(Account account);
	
	@Query("FROM Collection WHERE account = :account AND state = true ORDER BY name")
	public Iterable<Collection> findActives(Account account);
	
	@Query("FROM Collection WHERE account = :account ORDER BY createdDate DESC")
	public Iterable<Collection> findAllOrderByCreatedDate(Account account);
}
