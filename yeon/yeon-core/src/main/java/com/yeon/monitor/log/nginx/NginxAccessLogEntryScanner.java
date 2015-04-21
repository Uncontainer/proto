package com.yeon.monitor.log.nginx;

import com.yeon.monitor.common.MomString;
import com.yeon.monitor.log.ServerLogCollectOption.FileType;
import com.yeon.monitor.log.ServerLogCollectStatus;
import com.yeon.monitor.log.ServerLogEntryScanner;
import com.yeon.monitor.log.ServerLogLineScanner;

import java.io.IOException;
import java.util.Date;

public class NginxAccessLogEntryScanner implements ServerLogEntryScanner<NginxAccessLogEntry> {
	private final ServerLogLineScanner logLineScanner;

	private final long startTime;
	private final long endTime;

	private final MomString buffer;

	private final MomString line = new MomString();

	public NginxAccessLogEntryScanner(MomString buffer, ServerLogLineScanner logLineScanner, Date startDate, Date endDate) {
		super();
		if (startDate == null || endDate == null) {
			throw new IllegalArgumentException();
		}

		this.buffer = buffer;
		this.logLineScanner = logLineScanner;
		this.startTime = startDate.getTime();
		this.endTime = endDate.getTime();
	}

	@Override
	public int next(NginxAccessLogEntry logEntry) throws IOException {
		while (true) {
			int lineOffset = buffer.length();
			int writeCount = logLineScanner.nextLine(buffer);
			if (writeCount < 0) {
				buffer.setLength(lineOffset);
				return writeCount;
			}

			line.init(buffer.getBuffer(), lineOffset, writeCount);

			logEntry.init(line);

			long createTime = logEntry.getCreateTime();
			if (createTime == -1) {
				continue;
			}

			if (createTime < startTime) {
				buffer.setLength(0);
				continue;
			} else if (createTime > endTime) {
				buffer.setLength(lineOffset);
				return ServerLogCollectStatus.EOF;
			}

			if (!buffer.isFull()) {
				buffer.append('\n');
			}

			return writeCount;
		}
	}

	@Override
	public void back(NginxAccessLogEntry logEntry) {
		if (buffer.getLength() == 0) {
			throw new IllegalStateException("No content.");
		}

		int newLength = buffer.getLength() - logEntry.getContent().length() - 1;
		if (newLength > 0) {
			if (buffer.charAtOffset(newLength - 1) != '\n') {
				throw new IllegalArgumentException("Param log entry is not a last log entry.");
			}
		} else if (newLength == 0) {
			// 로그가 1개밖에 없을 경우 back()을 호출하면 index 값은 0이 된다.
		} else {
			throw new IllegalArgumentException("Param log entry is not a last log entry.");
		}

		buffer.setLength(newLength);
	}

	@Override
	public void clearReady() {
		return;
	}

	@Override
	public long getScanLogCount() {
		return logLineScanner.getScanLineCount();
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
	public FileType getFileType() {
		return FileType.NGINX_ACCESS;
	}

	@Override
	public void close() {
		if (logLineScanner != null) {
			logLineScanner.close();
		}
	}
}
