package com.naver.fog.field;

import java.util.HashMap;
import java.util.Map;

import com.naver.fog.IdeaResource;
import com.naver.fog.Resource;
import com.naver.fog.ResourceType;

public class Field extends IdeaResource {
	private FieldType type;
	Map<String, Object> options;

	@Override
	public ResourceType getType() {
		return ResourceType.FIELD;
	}

	public FieldType getFieldType() {
		return type;
	}

	public void setFieldType(FieldType type) {
		this.type = type;
	}

	@Override
	public void replaceWith(Resource other) {
		if (!(other instanceof Field)) {
			throw new IllegalArgumentException();
		}

		Field otherField = (Field)other;
		super.replaceWith(otherField);
		this.type = otherField.type;
		this.options = otherField.options;
	}

	public Map<String, Object> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}

	static final String PARAM_CARDINALITY = "cardinality";
	static final String PARAM_CANDIDATE = "candidate";

	public String getCardinality() {
		return getProperty(PARAM_CARDINALITY);
	}

	public void setCardinality(String cardinality) {
		setProperty(PARAM_CARDINALITY, cardinality);
	}

	public String getCandidate() {
		return getProperty(PARAM_CANDIDATE);
	}

	public void setCandidate(String candidate) {
		setProperty(PARAM_CANDIDATE, candidate);
	}

	@SuppressWarnings("unchecked")
	public <T> T getProperty(String name) {
		if (options == null) {
			return null;
		}

		return (T)options.get(name);
	}

	public <T> T getProperty(String name, T defaultValue) {
		T value = getProperty(name);
		if (value != null) {
			return value;
		}

		return defaultValue;
	}

	public void setProperty(String name, Object value) {
		if (value == null) {
			if (options == null) {
				return;
			} else {
				options.remove(name);
			}
		} else {
			if (options == null) {
				options = new HashMap<String, Object>();
			}
			options.put(name, value);
		}
	}
}
