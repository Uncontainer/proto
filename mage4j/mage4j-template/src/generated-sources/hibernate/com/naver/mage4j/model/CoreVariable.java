package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * CoreVariable generated by hbm2java
 */
@Entity
@Table(name = "core_variable"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = "code"))
public class CoreVariable implements java.io.Serializable {

	private Integer variableId;
	private String code;
	private String name;
	private Set<CoreVariableValue> coreVariableValues = new HashSet<CoreVariableValue>(0);

	public CoreVariable() {
	}

	public CoreVariable(String code, String name, Set<CoreVariableValue> coreVariableValues) {
		this.code = code;
		this.name = name;
		this.coreVariableValues = coreVariableValues;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "variable_id", unique = true, nullable = false)
	public Integer getVariableId() {
		return this.variableId;
	}

	public void setVariableId(Integer variableId) {
		this.variableId = variableId;
	}

	@Column(name = "code", unique = true)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreVariable")
	public Set<CoreVariableValue> getCoreVariableValues() {
		return this.coreVariableValues;
	}

	public void setCoreVariableValues(Set<CoreVariableValue> coreVariableValues) {
		this.coreVariableValues = coreVariableValues;
	}

}
