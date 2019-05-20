package com.are.gescob.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.are.gescob.entity.Account;
import com.are.gescob.entity.ManagementType;

public interface ManagementTypeRepository extends CrudRepository<ManagementType, Long> {
	
	@Query("FROM ManagementType ORDER BY name")
	public Iterable<ManagementType> findAllOrderByName();
	
	@Query("FROM ManagementType WHERE account = :account ORDER BY name")
	public Iterable<ManagementType> findAllOrderByName(@Param("account") Account account);
	
	@Query("FROM ManagementType WHERE account = :account AND state = true ORDER BY name")
	public Iterable<ManagementType> findActives(@Param("account") Account account);
	
	@Query("FROM ManagementType WHERE account = :account AND code = :code ORDER BY name")
	public Optional<ManagementType> findByCodeAndAccount(@Param("account") Account account, @Param("code") String code);
}
