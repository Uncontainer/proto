package com.naver.fog;

import java.util.Date;

import com.naver.fog.user.User;
import com.naver.fog.user.UserContext;

public class Blamable {
	protected long creatorId;
	protected Date createdDate;

	public long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public User getCreator() {
		return UserContext.getContext().getUser(creatorId);
	}

	public void replaceWith(Blamable other) {
		this.creatorId = other.creatorId;
		this.createdDate = other.createdDate;
	}
}
