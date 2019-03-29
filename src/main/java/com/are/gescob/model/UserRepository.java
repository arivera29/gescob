package com.are.gescob.model;

import org.springframework.data.repository.CrudRepository;

import com.are.gescob.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
	public Iterable<User> findAllByUserName(String username );
	public Iterable<User> findAllByEmail(String email );
	public Iterable<User> findAllOrderByName();
}
