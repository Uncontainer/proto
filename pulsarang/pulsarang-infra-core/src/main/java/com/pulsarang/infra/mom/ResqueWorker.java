package com.pulsarang.infra.mom;

import java.util.Map;

public interface ResqueWorker {
	String getName();

	boolean getAsync();

	/**
	 * 
	 * @param arguments
	 * @throws ReprocessableException
	 */
	void process(Map<String, Object> arguments);

	void reprocess(Map<String, Object> arguments, Map<String, Object> reprocessArguments);

	void verify(Map<String, Object> arguments, Map<String, Object> reprocessArguments);
}
