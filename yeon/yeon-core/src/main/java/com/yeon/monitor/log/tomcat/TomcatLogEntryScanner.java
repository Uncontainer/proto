package com.yeon.monitor.log.tomcat;

import com.yeon.monitor.common.MomString;
import com.yeon.monitor.log.ServerLogCollectOption.FileType;
import com.yeon.monitor.log.ServerLogCollectStatus;
import com.yeon.monitor.log.ServerLogEntryScanner;
import com.yeon.monitor.log.ServerLogLineScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TomcatLogEntryScanner implements ServerLogEntryScanner<TomcatLogEntry> {
	private final Logger log = LoggerFactory.getLogger(TomcatLogEntryScanner.class);

	private final ServerLogLineScanner logLineScanner;
	private final long startTime;
	private final long endTime;

	private final MomString buffer;
	private int lastLogOffset = 0;
	private int lastLogLength = 0;

	private boolean reachEndOfLog;
	private long scanLogCount;

	private final MomString logString = new MomString();

	Map<String, Object> cacheEntryMap = new HashMap<String, Object>();

	public TomcatLogEntryScanner(MomString buffer, ServerLogLineScanner logLineScanner, Date startDate, Date endDate) throws IOException {
		super();
		if (startDate == null || endDate == null) {
			throw new IllegalArgumentException();
		}

		this.logLineScanner = logLineScanner;
		this.startTime = startDate.getTime();
		this.endTime = endDate.getTime();

		this.buffer = buffer;

		moveNextLogEntryFirstLine();
		logString.reset();
	}

	@Override
	public FileType getFileType() {
		return FileType.TOMCAT;
	}

	@Override
	public long getScanLogCount() {
		return scanLogCount;
	}

	@Override
	public long getScanLineCount() {
		return logLineScanner.getScanLineCount();
	}

	@Override
	public long getScanFileSize() {
		return logLineScanner.getScanFileSize();
	}

	@Override
	public int next(TomcatLogEntry logEntry) throws IOException {
		int logLength = moveNextLogEntryFirstLine();
		if (logLength >= 0) {
			scanLogCount++;
			logEntry.init(logString);
		}

		return logLength;
	}

	@Override
	public void back(TomcatLogEntry logEntry) {
		if (lastLogOffset == 0) {
			throw new IllegalStateException("No content.");
		}

		int toOffset = lastLogOffset - logEntry.getContent().length() - 1;
		if (toOffset > 0) {
			if (buffer.charAtOffset(toOffset - 1) != '\n') {
				throw new IllegalArgumentException("Param log entry is not a last log entry.");
			}
		} else if (toOffset == 0) {
			// 로그가 1개밖에 없을 경우 back()을 호출하면 index 값은 0이 된다.
		} else {
			throw new IllegalArgumentException("Param log entry is not a last log entry.");
		}

		// shift left
		buffer.shiftByOffset(lastLogOffset, toOffset, lastLogLength);
		buffer.decreaseLength(lastLogOffset - toOffset);

		lastLogOffset = toOffset;
	}

	@Override
	public void clearReady() {
		buffer.setLength(lastLogOffset);
	}

	private int moveNextLogEntryFirstLine() throws IOException {
		if (reachEndOfLog) {
			return ServerLogCollectStatus.EOF;
		}

		boolean trimTail = false;
		while (true) {
			int lineOffset = buffer.length();
			int writeCount = logLineScanner.nextLine(buffer);
			if (writeCount >= 0) {
				if (buffer.isFull()) {
					break;
				}

				buffer.append('\n');
				writeCount++;

				lastLogLength += writeCount;
				if (lastLogLength > 50000) {
					if (!trimTail) {
						trimTail = true;
						buffer.insert(buffer.getLength() - writeCount, "...\n...\n");
						lastLogLength += 8;
						log.debug("[YEON] Too big log entry.\n{}......", MomString.createMutableString(buffer.getBuffer(), lastLogOffset, lastLogLength < 1000 ? lastLogLength : 1000));
					}
				}

				long createTime;
				// 로그의 첫 라인은 "yyyy-MM-dd HH:mm:ss"포맷의 날짜로 시작하며 여기에 맞지 않는 데이터는 검사에서 제외한다.
				char ch = buffer.charAtOffset(lineOffset);
				if (writeCount < 19 || ch < '0' || ch > '9'
					|| (createTime = TomcatLogDateParser.parseDate(buffer.getBuffer(), lineOffset)) == -1) {
					if (trimTail) {
						lastLogLength -= writeCount;
						buffer.setLength(buffer.getLength() - writeCount);
					}

					continue;
				}

				if (createTime < startTime) {
					buffer.clear();
					lastLogOffset = 0;
					lastLogLength = 0;
					continue;
				} else if (createTime > endTime) {
					reachEndOfLog = true;
					if (lastLogLength - writeCount < 1) {
						return ServerLogCollectStatus.EOF;
					}
				}
			} else if (writeCount == ServerLogCollectStatus.EOF) {
				reachEndOfLog = true;
				buffer.setLength(lineOffset);
				writeCount = 0;
			} else {
				return writeCount;
			}

			int logLength = lastLogLength - writeCount;

			logString.init(buffer.getBuffer(), lastLogOffset, logLength);
			logString.trimRightNewLine(false);

			lastLogOffset += logLength;
			lastLogLength = writeCount;

			return logLength;
		}

		reachEndOfLog = true;
		return ServerLogCollectStatus.EOF;
	}

	@Override
	public void close() {
		if (logLineScanner != null) {
			logLineScanner.close();
		}
	}
}
