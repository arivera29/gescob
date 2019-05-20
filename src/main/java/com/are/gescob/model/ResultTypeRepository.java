package com.are.gescob.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.are.gescob.entity.Account;
import com.are.gescob.entity.ResultType;



public interface ResultTypeRepository extends CrudRepository<ResultType, Long> {
	
	@Query("FROM ResultType ORDER BY name")
	public Iterable<ResultType> findAllOrderByName();
	
	@Query("FROM ResultType WHERE account = :account ORDER BY name")
	public Iterable<ResultType> findAllOrderByName(@Param("account") Account account);
	
	@Query("FROM ResultType WHERE account = :account AND state = true ORDER BY name")
	public Iterable<ResultType> findActives(@Param("account") Account account);
	
	@Query("FROM ResultType WHERE account = :account AND code = :code ORDER BY name")
	public Optional<ResultType> findByCodeAndAccount(@Param("account") Account account, @Param("code") String code);
	
}
