package com.are.gescob.model;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.are.gescob.entity.Account;
import com.are.gescob.entity.User;



public interface UserRepository extends CrudRepository<User, Long> {

	@Query("FROM User ORDER BY name")
	public Iterable<User> findAllOrderByName();
	
	@Query("FROM User WHERE account = :account ORDER BY name")
	public Iterable<User> findAllOrderByName(Account account);
	
	@Query("FROM User WHERE username= :username")
	public User findByUsername(String username);
	
	@Query("FROM User WHERE username= :username AND id != :id")
	public User findByUsernameAndDiferentId(String username, Long id );
	
	@Query("FROM User WHERE username= :user AND password = :password")
	public User findByUsernameAndPassword(String user, String password);
	
	@Query("FROM User WHERE username= :user AND email = :email")
	public User findByUsernameAndEmail(String user, String email);
	
	@Transactional
	@Query("UPDATE User SET password = :password WHERE username= :username")
	public void updatePassword(String username, String password);
	
	
	
}
