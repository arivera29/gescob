package com.are.gescob.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name="users",
	uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@NotEmpty
	@Size(max=200, message = "Max size 200 characteres")
	private String username;
	@NotEmpty
	@Size(max=200, message = "Max size 200 characteres")
	private String name;
	@NotEmpty (message="No empty value")
	@Email (message = "Email no valid")
	private String email;
	@NotEmpty (message="No empty value")
	@Size(min=8,max=200, message = "Size between 8 and 200 characteres")
	private String password;
	private Integer state;
	private Boolean blocked;
	@ManyToOne
	private Profile profile;
	private byte[] photo;
	private java.util.Date createdDate;
	private java.util.Date lastChangePasswordDate;
	
	//Location
	
	private Double latitud;
	private Double longitud;
	private java.util.Date lastDateLocation;
	
	@Transient
	private String repeatPassword;
	
	
	
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
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Boolean getBlocked() {
		return blocked;
	}
	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	public byte[] getPhoto() {
		return photo;
	}
	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	public java.util.Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}
	public java.util.Date getLastChangePasswordDate() {
		return lastChangePasswordDate;
	}
	public void setLastChangePasswordDate(java.util.Date lastChangePasswordDate) {
		this.lastChangePasswordDate = lastChangePasswordDate;
	}
	public Double getLatitud() {
		return latitud;
	}
	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}
	public Double getLongitud() {
		return longitud;
	}
	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}
	public java.util.Date getLastDateLocation() {
		return lastDateLocation;
	}
	public void setLastDateLocation(java.util.Date lastDateLocation) {
		this.lastDateLocation = lastDateLocation;
	}
	public String getRepeatPassword() {
		return repeatPassword;
	}
	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}
	
	@Transient
	public Boolean isPasswordsEquals() {
		return this.password==this.repeatPassword;
	}
	
	
	
}
