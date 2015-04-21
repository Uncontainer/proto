package com.yeon.monitor.log;

import com.yeon.monitor.common.MomString;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FileServerLogLineScanner implements ServerLogLineScanner {
	private final Logger log = LoggerFactory.getLogger(FileServerLogLineScanner.class);

	private final ServerLogCollectOption option;
	private Reader reader;

	private final long startTime;
	private Date currentDay;

	private long scanFileSize;
	private long scanLineCount;
	private int scanFileCount;

	private final char[] buffer = new char[8192];
	private int offset;
	private int count;

	public FileServerLogLineScanner(ServerLogCollectOption option) {
		this.option = option;
		this.currentDay = option.getStartDay();
		if (currentDay == null) {
			throw new IllegalArgumentException();
		}

		this.reader = nextReader();
		startTime = System.currentTimeMillis();
	}

	@Override
	public long getScanFileSize() {
		return scanFileSize;
	}

	@Override
	public long getScanLineCount() {
		return scanLineCount;
	}

	@Override
	public int nextLine(MomString outBuffer) throws IOException {
		if (reader == null) {
			if (scanFileCount == 0) {
				throw new FileNotFoundException("Fail to find file: " + option.getFilePath());
			}

			return ServerLogCollectStatus.EOF;
		}

		boolean eol = false;
		int lineOffset = outBuffer.getLength();
		while (true) {
			int start = offset;
			int end = count - 1;
			while (offset < count) {
				char ch = buffer[offset++];
				if (ch == '\n') {
					eol = true;
					end = offset - 2;

					break;
				}
			}

			if (start <= end) {
				if (buffer[end] == '\r') {
					end--;
				}

				try {
					outBuffer.append(buffer, start, end - start + 1);
				} catch (IndexOutOfBoundsException e) {
					return ServerLogCollectStatus.BUFFER_OVERFLOW;
				}
			}

			if (eol) {
				break;
			}

			offset = 0;
			count = reader.read(buffer, 0, buffer.length);
			if (count == -1) {
				reader = nextReader();
				if (reader == null) {
					break;
				}
			} else {
				scanFileSize += count;
			}
		}

		if (!eol && outBuffer.getLength() == lineOffset) {
			return ServerLogCollectStatus.EOF;
		}

		scanLineCount++;

		if (scanLineCount % 100 == 1) {
			if (System.currentTimeMillis() - startTime > option.getTimeout()) {
				if (option.getTimeout() < 0) {
					return option.getTimeout();
				} else {
					return ServerLogCollectStatus.TIMEOUT;
				}
			}

			if (scanLineCount % 100000 == 1) {
				log.debug("[YEON] Scan log {} lines.", scanLineCount);
			}
		}

		return outBuffer.getLength() - lineOffset;
	}

	@Override
	public void close() {
		IOUtils.closeQuietly(reader);
	}

	private Reader nextReader() {
		while (true) {
			if (reader != null) {
				currentDay = new Date(currentDay.getTime() + TimeUnit.DAYS.toMillis(1));
			}

			if (currentDay.after(option.getEndDay())) {
				return null;
			}

			IOUtils.closeQuietly(reader);

			try {
				File file = getLogFile(currentDay);
				reader = new FileReader(file);
				scanFileCount++;

				log.debug("scan log file {}", file);

				return reader;
			} catch (FileNotFoundException e) {
				reader = null;
				currentDay = new Date(currentDay.getTime() + TimeUnit.DAYS.toMillis(1));
				continue;
			}
		}
	}

	private File getLogFile(Date day) {
		File file;
		if (currentDay.equals(option.getToday())) {
			file = new File(option.getFilePath());
		} else {
			file = new File(option.getFilePath() + "." + new SimpleDateFormat("yyyy-MM-dd").format(day));
		}

		return file;
	}
}
