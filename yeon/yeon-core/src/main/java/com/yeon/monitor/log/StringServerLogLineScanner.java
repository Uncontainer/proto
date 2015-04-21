package com.yeon.monitor.log;

import com.yeon.monitor.common.MomString;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class StringServerLogLineScanner implements ServerLogLineScanner {
	private final String content;
	private final BufferedReader reader;
	private long scanLineCount = 0;

	public StringServerLogLineScanner(String content) {
		this.content = content;
		reader = new BufferedReader(new StringReader(content));
	}

	public String getContent() {
		return content;
	}

	@Override
	public long getScanFileSize() {
		return content.length();
	}

	@Override
	public long getScanLineCount() {
		return scanLineCount;
	}

	@Override
	public int nextLine(MomString buffer) throws IOException {
		// TODO 버퍼 기반으로 변경.
		String str = reader.readLine();

		if (str == null) {
			return ServerLogCollectStatus.EOF;
		} else if (str.isEmpty()) {
			return 0;
		}

		char[] charArray = str.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			try {
				buffer.append(charArray[i]);
			} catch (IndexOutOfBoundsException e) {
				return ServerLogCollectStatus.BUFFER_OVERFLOW;
			}
		}

		scanLineCount++;

		return charArray.length;
	}

	@Override
	public void close() {
		IOUtils.closeQuietly(reader);
	}
}
