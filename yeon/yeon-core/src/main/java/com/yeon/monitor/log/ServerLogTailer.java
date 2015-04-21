package com.yeon.monitor.log;

import com.yeon.monitor.common.MomString;
import com.yeon.monitor.log.ServerLogCollectOption.FileType;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class ServerLogTailer implements ServerLogEntryScanner<ServerLogEntry> {
	private final ServerLogCollectOption option;

	private long scanFileSize = -1;
	private boolean scan = false;

	private final MomString buffer;

	public ServerLogTailer(MomString buffer, ServerLogCollectOption option) {
		this.buffer = buffer;
		this.option = option;
		if (option.getFilePath() == null) {
			throw new IllegalArgumentException("Null file path.");
		}
	}

	@Override
	public int next(ServerLogEntry logEntry) throws IOException {
		if (scan) {
			return ServerLogCollectStatus.EOF;
		}

		Reader reader = null;
		FileInputStream fis = null;
		try {
			File file = new File(option.getFilePath());
			int maxScanSize = option.getMaxScanSize();
			fis = new FileInputStream(file);

			long fileLenth = file.length();
			if (fileLenth > maxScanSize) {
				long skipRemain = fileLenth - maxScanSize;
				while (skipRemain > 0L) {
					long nSkip = fis.skip(skipRemain);
					if (nSkip <= 0) {
						break;
					}
					skipRemain -= nSkip;
				}

				reader = new InputStreamReader(fis);
				// 중간에 로그가 끊기는 첫 라인은 제거한다.
				while (reader.read() != '\n') {
					continue;
				}
			} else {
				maxScanSize = (int)fileLenth;
				reader = new FileReader(file);
			}

			// 읽는 시점에 파일이 커지는 것을 대비하여 20% 더 여유있게 크기를 잡는다.
			scanFileSize = 0;
			int length = buffer.getBuffer().length;
			int offset = 0;
			int read;
			while ((read = reader.read(buffer.getBuffer(), offset, length)) != -1) {
				scanFileSize += read;
				offset += read;
				length -= read;
			}
			buffer.setLength(offset);

			logEntry.getContent().init(buffer.getBuffer(), 0, offset);

			return offset;
		} finally {
			IOUtils.closeQuietly(fis);
			IOUtils.closeQuietly(reader);
			scan = true;
		}
	}

	@Override
	public void back(ServerLogEntry logEntry) {
		scanFileSize = -1;
		scan = false;
	}

	@Override
	public void clearReady() {
		return;
	}

	@Override
	public long getScanLogCount() {
		return -1;
	}

	@Override
	public long getScanLineCount() {
		return -1;
	}

	@Override
	public long getScanFileSize() {
		return scanFileSize;
	}

	@Override
	public FileType getFileType() {
		return option.getFileType();
	}

	@Override
	public void close() {
		// do nothing
	}
}
