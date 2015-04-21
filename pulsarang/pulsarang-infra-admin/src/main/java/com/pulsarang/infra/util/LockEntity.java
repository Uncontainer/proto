package com.pulsarang.infra.util;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "nm_lck")
public class LockEntity {
	@Id
	@Column(name = "lck_nm", length = 100)
	private String name;

	@Column(name = "lck_ymdt", nullable = false)
	private Date lockTime;

	@Column(name = "lck_max_dr", nullable = false)
	private long maxLockDuration;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getLockTime() {
		return lockTime;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	public long getMaxLockDuration() {
		return maxLockDuration;
	}

	public void setMaxLockDuration(long maxLockDuration) {
		this.maxLockDuration = maxLockDuration;
	}
}
