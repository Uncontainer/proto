package com.pulsarang.infra.config.info;

import com.pulsarang.infra.config.ConfigEntityId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tkt_info")
public class ConfigInfoEntity {
	@Id
	ConfigEntityId id;

	// 티켓 설명
	@Column(name = "tkt_desc", length = 1000, nullable = true)
	String description;

	// 상위 tkt_id 목록
	@Column(name = "supr_tkt_nms", length = 300, nullable = true)
	String superConfigNames;

	// 추상 configuraion 여부
	@Column(name = "abstract")
	boolean isAbstract = false;

	// 상태
	@Column(name = "stat_cd", length = 6, nullable = false)
	String status;

	@Column(name = "sln_nm", length = 20, nullable = true)
	String solutionName;

	@Column(name = "pjt_nm", length = 30, nullable = true)
	String projectName;

	public ConfigEntityId getId() {
		return id;
	}

	public void setId(ConfigEntityId id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSuperConfigNames() {
		return superConfigNames;
	}

	public void setSuperConfigNames(String superConfigNames) {
		this.superConfigNames = superConfigNames;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isStop() {
		return "0".equals(status);
	}

	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}
