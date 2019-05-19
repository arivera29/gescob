package com.are.gescob.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="users", 
uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@NotBlank(message = "{valid.notblank}")
	private String username;
	@NotBlank(message = "{valid.notblank}")
	private String name;
	@NotBlank(message = "{valid.notblank}")
	@Email(message ="{valid.email}")
	private String email;
	private String password;
	private Boolean active;
	@NotBlank(message = "{valid.notblank}")
	private String role;
	
	private Boolean parent;
	
	@ManyToOne
	private Account account;
	
	private Boolean changePassword;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getChangePassword() {
		return changePassword;
	}
	public void setChangePassword(Boolean changePassword) {
		this.changePassword = changePassword;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Boolean getParent() {
		return parent;
	}
	public void setParent(Boolean parent) {
		this.parent = parent;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	
	
	

}
