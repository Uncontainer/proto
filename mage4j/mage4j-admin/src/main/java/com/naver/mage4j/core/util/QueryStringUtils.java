package com.naver.mage4j.core.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.inject.internal.Lists;

public class QueryStringUtils {

	public static Map<String, Object> parseQuery(String query) {
		if (query == null) {
			return Collections.emptyMap();
		}

		Map<String, Object> result = new LinkedHashMap<String, Object>();
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			int sepPoint = pair.indexOf("=");
			if (sepPoint > 0) {
				String key = pair.substring(0, sepPoint);
				String value = pair.substring(sepPoint + 1);
				Object saved = result.get(key);
				if (saved != null) {
					if (saved instanceof List) {
						((List)saved).add(value);
					} else {
						result.put(key, Lists.newArrayList(saved, value));
					}
				} else {
					result.put(key, value);
				}
			} else if (sepPoint == 0) {
				continue;
			} else {
				result.put(pair, "");
			}
		}

		return result;
	}

	public static String buildQuery(Map<String, Object> params, String sep) {
		if (params == null || params.isEmpty()) {
			return "";
		}

		if (sep == null) {
			sep = "&";
		}

		StringBuilder query = new StringBuilder();
		for (Entry<String, Object> pair : params.entrySet()) {
			Object value = pair.getValue();
			if (value instanceof List) {
				for (Object o : (List)value) {
					query.append(pair.getKey()).append("=").append(o != null ? o.toString() : "");
				}
			} else {
				if (query.length() != 0) {
					query.append(sep);
				}
				query.append(pair.getKey()).append("=").append(value != null ? value.toString() : "");
			}
		}

		return query.toString();
	}

	public static String buildQuery(Map<String, Object> params) {
		return buildQuery(params, "&");
	}
}
