package com.naver.fog;

public abstract class Resource extends Blamable {
	public static final long NULL_ID = 0L;

	protected long id;
	protected String name;
	protected String description;

	public abstract ResourceType getType();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void replaceWith(Resource other) {
		if (this.id != other.id) {
			throw new IllegalArgumentException("Resource ID must be equal.");
		}

		super.replaceWith(other);
		this.name = other.name;
		this.description = other.description;
	}
}
