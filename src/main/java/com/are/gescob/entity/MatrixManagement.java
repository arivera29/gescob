package com.are.gescob.entity;

import java.beans.Transient;
import java.text.SimpleDateFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="matrix_management")
public class MatrixManagement {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@ManyToOne
	private ManagementType managementType;
	@ManyToOne
	private ResultType resultType;
	@ManyToOne
	private AnomalyType anomalyType;
	
	@DateTimeFormat
	private java.util.Date createdDate;
	@ManyToOne
	private User createdUser;
	
	@ManyToOne
	private Account account;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ManagementType getManagementType() {
		return managementType;
	}
	public void setManagementType(ManagementType managementType) {
		this.managementType = managementType;
	}
	public ResultType getResultType() {
		return resultType;
	}
	public void setResultType(ResultType resultType) {
		this.resultType = resultType;
	}
	public AnomalyType getAnomalyType() {
		return anomalyType;
	}
	public void setAnomalyType(AnomalyType anomalyType) {
		this.anomalyType = anomalyType;
	}
	public java.util.Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}
	public User getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(User createdUser) {
		this.createdUser = createdUser;
	}
	
	
	
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	@Transient
	public String getCreatedDate_toString() {
		java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(this.createdDate);
	}

}
