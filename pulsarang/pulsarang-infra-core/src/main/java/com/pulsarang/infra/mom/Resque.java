package com.pulsarang.infra.mom;

import java.util.Map;

public interface Resque {
	void enqueue(Map<String, Object> arguments);

	void enqueue(Map<String, Object> arguments, boolean async);
}
