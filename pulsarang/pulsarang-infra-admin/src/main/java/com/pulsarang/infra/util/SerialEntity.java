package com.pulsarang.infra.util;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cmp_serial")
public class SerialEntity {
	@Id
	@Column(name = "srl_nm")
	String name;

	@Column(name = "srl_no")
	long serial;
}
