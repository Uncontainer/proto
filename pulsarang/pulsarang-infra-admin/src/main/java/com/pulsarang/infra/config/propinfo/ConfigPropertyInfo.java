package com.pulsarang.infra.config.propinfo;

import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tkt_prop_info")
public class ConfigPropertyInfo {
	@Id
	ConfigPropertyInfoId id;

	// 설정값 카테고리
	@Column(name = "tkt_prop_catg", length = 50, nullable = false)
	String category;

	// 필수 필드 여부
	@Column(name = "required")
	boolean required = false;

	// 수정 가능 여부
	@Column(name = "modifiable")
	boolean modifiable = true;

	// 데이터 type 코드
	@Column(name = "default_val", length = 10, nullable = false)
	String dataTypeCode;

	// 설명
	@Column(name = "tkt_prop_desc", length = 1000, nullable = true)
	String description;

	// validation expression
	@Column(name = "vldt_expr", length = 1000, nullable = true)
	String validationExpression;

	// 기본 값
	@Column(name = "default_val_cont", length = 1000, nullable = true)
	String defaultValue;

	@Transient
	List<String> candidates = Collections.emptyList();

	public ConfigPropertyInfoId getId() {
		return id;
	}

	public void setId(ConfigPropertyInfoId id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isModifiable() {
		return modifiable;
	}

	public void setModifiable(boolean modifiable) {
		this.modifiable = modifiable;
	}

	public String getDataTypeCode() {
		return dataTypeCode;
	}

	public void setDataTypeCode(String dataTypeCode) {
		this.dataTypeCode = dataTypeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValidationExpression() {
		return validationExpression;
	}

	public void setValidationExpression(String validationExpression) {
		this.validationExpression = validationExpression;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public List<String> getCandidates() {
		return candidates;
	}

	public void setCandidates(List<String> candidates) {
		this.candidates = candidates;
	}

	public String toString(Object obj) {
		if (obj == null) {
			return null;
		}

		// TODO dataType에 따른 처리 추가.
		return obj.toString();
	}
}
