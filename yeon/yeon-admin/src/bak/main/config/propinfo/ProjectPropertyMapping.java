package com.pulsarang.infra.config.propinfo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pjt_tkt_prop_mpng")
public class ProjectPropertyMapping {
	@Id
	ProjectPropertyMappingId id;

	public ProjectPropertyMappingId getId() {
		return id;
	}

	public void setId(ProjectPropertyMappingId id) {
		this.id = id;
	}

}
