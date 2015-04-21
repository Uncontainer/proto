package com.pulsarang.infra.config;

/**
 * TODO 구현 추가.
 * 
 * @author pulsarang
 * 
 */
public interface ConfigRefresher {
	void refresh();

	void validate();

	void remove();
}
