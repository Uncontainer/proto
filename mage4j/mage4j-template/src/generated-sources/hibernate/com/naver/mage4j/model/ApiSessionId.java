package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ApiSessionId generated by hbm2java
 */
@Embeddable
public class ApiSessionId implements java.io.Serializable {

	private int userId;
	private Date logdate;
	private String sessid;

	public ApiSessionId() {
	}

	public ApiSessionId(int userId, Date logdate) {
		this.userId = userId;
		this.logdate = logdate;
	}

	public ApiSessionId(int userId, Date logdate, String sessid) {
		this.userId = userId;
		this.logdate = logdate;
		this.sessid = sessid;
	}

	@Column(name = "user_id", nullable = false)
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Column(name = "logdate", nullable = false, length = 19)
	public Date getLogdate() {
		return this.logdate;
	}

	public void setLogdate(Date logdate) {
		this.logdate = logdate;
	}

	@Column(name = "sessid", length = 40)
	public String getSessid() {
		return this.sessid;
	}

	public void setSessid(String sessid) {
		this.sessid = sessid;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ApiSessionId))
			return false;
		ApiSessionId castOther = (ApiSessionId)other;

		return (this.getUserId() == castOther.getUserId())
			&& ((this.getLogdate() == castOther.getLogdate()) || (this.getLogdate() != null && castOther.getLogdate() != null && this.getLogdate().equals(castOther.getLogdate())))
			&& ((this.getSessid() == castOther.getSessid()) || (this.getSessid() != null && castOther.getSessid() != null && this.getSessid().equals(castOther.getSessid())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getUserId();
		result = 37 * result + (getLogdate() == null ? 0 : this.getLogdate().hashCode());
		result = 37 * result + (getSessid() == null ? 0 : this.getSessid().hashCode());
		return result;
	}

}