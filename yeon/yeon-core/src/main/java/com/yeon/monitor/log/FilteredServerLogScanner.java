package com.yeon.monitor.log;

import com.yeon.monitor.log.ServerLogCollectOption.FileType;

import java.io.IOException;

public class FilteredServerLogScanner<T extends ServerLogEntry> implements ServerLogEntryScanner<T> {
	private final ServerLogEntryScanner<T> scanner;
	private final ServerLogEntryFilter<T> filter;

	private long matchCount;

	public FilteredServerLogScanner(ServerLogEntryScanner<T> scanner, ServerLogEntryFilter<T> filter) {
		this.scanner = scanner;
		this.filter = filter;
	}

	@Override
	public int next(T logEntry) throws IOException {
		int nWrite;
		while ((nWrite = scanner.next(logEntry)) >= 0) {
			if (filter.accept(logEntry)) {
				matchCount++;
				return nWrite;
			} else {
				scanner.back(logEntry);
			}
		}

		return nWrite;
	}

	@Override
	public void back(T logEntry) {
		scanner.back(logEntry);
	}

	@Override
	public void clearReady() {
		scanner.clearReady();
	}

	@Override
	public long getScanLogCount() {
		return scanner.getScanLogCount();
	}

	@Override
	public long getScanLineCount() {
		return scanner.getScanLineCount();
	}

	@Override
	public long getScanFileSize() {
		return scanner.getScanFileSize();
	}

	public long getMatchCount() {
		return matchCount;
	}

	@Override
	public FileType getFileType() {
		return scanner.getFileType();
	}

	@Override
	public void close() {
		if (scanner != null) {
			scanner.close();
		}
	}
}
