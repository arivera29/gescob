package com.are.gescob.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.are.gescob.entity.Account;
import com.are.gescob.entity.MatrixManagement;

public interface MatrixManagementRepository extends CrudRepository<MatrixManagement, Long> {

	@Query("FROM MatrixManagement ORDER BY name")
	public Iterable<MatrixManagement> findAllOrderByName();
	
	@Query("FROM MatrixManagement WHERE account = :account ORDER BY name")
	public Iterable<MatrixManagement> findAllOrderByName(@Param("account") Account account);
	
	@Query("FROM MatrixManagement WHERE account = :account AND state = true ORDER BY name")
	public Iterable<MatrixManagement> findActives(@Param("account") Account account);
}
