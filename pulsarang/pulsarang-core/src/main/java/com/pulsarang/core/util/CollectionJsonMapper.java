package com.pulsarang.core.util;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

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

	public String toJson(Map<String, Object> map) {
		try {
			return mapper.writeValueAsString(map);
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

	public String toJson(@SuppressWarnings("rawtypes") List list) {
		try {
			return mapper.writeValueAsString(list);
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
