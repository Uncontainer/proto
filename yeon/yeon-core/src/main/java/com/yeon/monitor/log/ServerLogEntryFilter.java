package com.yeon.monitor.log;

public interface ServerLogEntryFilter<T extends ServerLogEntry> {
	boolean accept(T logEntry);
}
