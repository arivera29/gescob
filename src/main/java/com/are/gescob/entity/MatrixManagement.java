package com.are.gescob.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	
	

}
