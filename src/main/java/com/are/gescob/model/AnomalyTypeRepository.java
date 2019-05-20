package com.are.gescob.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.are.gescob.entity.Account;
import com.are.gescob.entity.AnomalyType;

public interface AnomalyTypeRepository extends CrudRepository<AnomalyType, Long> {
	@Query("FROM AnomalyType ORDER BY name")
	public Iterable<AnomalyType> findAllOrderByName();
	
	@Query("FROM AnomalyType WHERE account = :account ORDER BY name")
	public Iterable<AnomalyType> findAllOrderByName(@Param("account") Account account);
	
	@Query("FROM AnomalyType WHERE account = :account AND state = true ORDER BY name")
	public Iterable<AnomalyType> findActives(@Param("account") Account account);
	
	@Query("FROM AnomalyType WHERE account = :account AND code = :code ORDER BY name")
	public Optional<AnomalyType> findByCodeAndAccount(@Param("account") Account account, @Param("code") String code);
}
