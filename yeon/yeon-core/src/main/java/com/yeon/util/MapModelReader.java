package com.yeon.util;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapModelReader<T extends MapModel> {
	Reader reader;
	Class<T> clazz;

	public MapModelReader(String json, Class<T> clazz) {
		this(new StringReader(json), clazz);
	}

	public MapModelReader(Reader reader, Class<T> clazz) {
		this.reader = reader;
		this.clazz = clazz;
	}

	public List<T> list() {
		ObjectMapper mapper = CollectionJsonMapper.getMapper();
		List<Map<String, Object>> mapList;
		try {
			mapList = mapper.readValue(reader, List.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		List<T> result = new ArrayList<T>();
		for (Map<String, Object> map : mapList) {
			result.add(MapModel.fromMap(map, clazz));
		}

		return result;
	}

	public T get() {
		ObjectMapper mapper = CollectionJsonMapper.getMapper();
		try {
			Map<String, Object> map = mapper.readValue(reader, Map.class);
			return MapModel.fromMap(map, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void close() throws IOException {
		reader.close();
	}
}
