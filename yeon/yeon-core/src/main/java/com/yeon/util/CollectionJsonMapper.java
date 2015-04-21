package com.yeon.util;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.List;
import java.util.Map;

public class CollectionJsonMapper {
	private static final CollectionJsonMapper INSTANCE = new CollectionJsonMapper();

	public static CollectionJsonMapper getInstance() {
		return INSTANCE;
	}

	public static ObjectMapper getMapper() {
		return INSTANCE.mapper;
	}

	ObjectMapper mapper = new ObjectMapper();

	private CollectionJsonMapper() {
	}

	public String toJson(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> toMap(String json) {
		try {
			return mapper.readValue(json, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> toList(String json) {
		try {
			return mapper.readValue(json, List.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
